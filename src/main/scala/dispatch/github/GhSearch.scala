package dispatch.github

import scala.concurrent.Future
import dispatch._
import Defaults._
import net.liftweb.json._

sealed abstract class Sort
case class Stars() extends Sort
case class BestMatch() extends Sort
case class Forks() extends Sort
case class Updated() extends Sort

case class GhCode(name : String, path : String, sha : String,
                  url : String, git_url : String, html_url : String,
                  score : Double, repository : GhRSimpleRepository)

object GhSearch {

  implicit val formats = DefaultFormats

  def search_repos(query : String, sort : Sort = BestMatch(),
                   ascending : Boolean = false) : Future[List[GhRepository]] = {
    val svc = GitHub.api_host / "search" / "repositories"
    val params1 = Map("q" -> query,
                      "order" -> (if (ascending) "asc" else "desc"))
    val params = sort match {
      case BestMatch() => params1
      case Stars() => params1 + ("order" -> "stars")
      case Updated() => params1 + ("order" -> "updated")
      case Forks() => params1 + ("order" -> "forks")
    }

    val respJson = Http(svc.secure <<? params OK as.lift.Json)
    for (js <- respJson) yield (js \\ "items").extract[List[GhRepository]]
  }

  def search_code(query : String,
                  sortByIndexed : Boolean = false,
                  ascending : Boolean = false) : Future[List[GhCode]] = {
    val svc = GitHub.api_host / "search" / "code"
    val params1 = Map("q" -> query,
                      "order" -> (if (ascending) "asc" else "desc"))
    val params =
      if (sortByIndexed) params1 + ("order" -> "indexed") else params1

    val respJson = Http(svc.secure <<? params OK as.lift.Json)
    for (js <- respJson) yield (js \\ "items").extract[List[GhCode]]
  }
}