package dispatch.github

import scala.concurrent._
import scala.concurrent.duration._
import scala.async.Async.{async, await}
import dispatch.{Req, Http, as, url}
import net.liftweb.json._
import com.ning.http.client.Response
import com.typesafe.scalalogging.slf4j.LazyLogging

trait Client extends LazyLogging {

  def searchUsers(q : String) = new UserSearch(this, q)

  def searchCode(q : String) = new CodeSearch(this, q)

  def searchRepos(q : String) = new RepoSearch(this, q)

  private[github] def pause(resp : Response)(implicit ec : ExecutionContext)
                           : Future[Boolean] = Future {
    val hdrs = resp.getHeaders()
    hdrs.getFirstValue("X-RateLimit-Remaining") match {
      case "0" => {
        val reset = 1000 * hdrs.getFirstValue("X-RateLimit-Reset").toLong
        val now = System.currentTimeMillis()
        // An extra second for clock skew.
        val delay = math.max(1000, 1000 + reset - now)
        logger.debug(s"GitHub API rate limit reached: pausing for ${delay / 1000}ms")
        Thread.sleep(delay)
        true
      }
      case _ => false
    }
  }

  def request(req : Req)
             (implicit ec : ExecutionContext) : Future[Response] = async {
    val resp = await(Http(req))
    resp.getStatusCode() match {
      case 403 => {
        if (await(pause(resp))) {
          await(request(req))
        }
        else {
          resp
        }
      }    
      case _ => {
        await(pause(resp))
        resp
      }
    }
  }

  private[github] def searchByUrl[T](req : Req)
   (implicit m : Manifest[T], ec : ExecutionContext) 
                                    : Future[Stream[T]] = async {
    implicit val formats = DefaultFormats

    val resp = await(this.request(req))
    val json = as.lift.Json(resp)
    val results = Stream((json \\ "items").extract[List[T]] :_*)
    nextPage(resp) match {
      case None => results
      case Some(nextUrl) => {
        lazy val rest = Await.result(searchByUrl(url(nextUrl)), Duration.Inf)
        results.append(rest)
      }
    }
  }

}

class OAuthClient(accessToken : String) extends Client {

  override def request(req : Req) 
                      (implicit ec : ExecutionContext) : Future[Response] =
    super.request(req.addHeader("Authorizaton", "token " + accessToken))

}

object BasicClient extends Client {

}
