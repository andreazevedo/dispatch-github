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

}