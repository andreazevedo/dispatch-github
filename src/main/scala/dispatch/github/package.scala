package dispatch

import scala.concurrent._
import scala.concurrent.duration._
import scala.async.Async.{async, await}
import com.ning.http.client.Response

package object github {


  private val nextPageRegex = """(?<=.*<).*(?=>; rel="next".*)""".r

  /** Returns a URL to the next page of results, if present in the "Link"
   *  header.
   *
   *  https://developer.github.com/v3/#pagination
   */
  private[github] def nextPage(resp : Response) : Option[String] =
    resp.getHeaders().getFirstValue("Link") match {
      case null => None
      case str => nextPageRegex.findFirstIn(str)
    }

  private[github] def pause(resp : Response)(implicit ec : ExecutionContext) 
                           : Future[Boolean] = Future {
    val hdrs = resp.getHeaders()
    hdrs.getFirstValue("X-RateLimit-Remaining") match {
      case "0" => {
        val reset = 1000 * hdrs.getFirstValue("X-RateLimit-Reset").toLong
        val now = System.currentTimeMillis()
        // An extra second for clock skew.
        val delay = math.max(1000, 1000 + reset - now)
        Thread.sleep(delay)
        true
      }
      case _ => false
    }
  }

}