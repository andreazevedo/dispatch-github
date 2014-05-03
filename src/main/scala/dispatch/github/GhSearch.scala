package dispatch.github

import scala.concurrent._
import scala.concurrent.duration._
import dispatch._
import com.ning.http.client.Response

case class GhCode(name : String, path : String, sha : String,
                  url : String, git_url : String, html_url : String,
                  score : Double, repository : GhRSimpleRepository)

class UserSearch private(client : Client, params : Map[String, String]) {

  def this(client : Client, q : String) = this(client, Map("q" -> q))

  def ascending() = new UserSearch(client, params + ("sort" -> "asc"))

  def byFollowers() = new UserSearch(client, params + ("order" -> "followers"))

  def byRepositories() =
    new UserSearch(client, params + ("order" -> "repositories"))

  def byJoined() = new UserSearch(client, params + ("order" -> "joined"))

  def perPage(n : Int) =
    new UserSearch(client, params + ("per_page" -> n.toString))

  def search()(implicit ec : ExecutionContext) = {
    val url = (GitHub.api_host / "search" / "users" <<? params).secure
    client.searchByUrl[GhAuthor](url)
  }

}

class CodeSearch private(client : Client, params : Map[String, String]) {

  def this(client : Client, q : String) = this(client, Map("q" -> q))

  def byIndexed() = new CodeSearch(client, params + ("ordered" -> "indexed"))

  def ascending() = new CodeSearch(client, params + ("sort" -> "asc"))

  def search()(implicit ec : ExecutionContext) = {
    val url = (GitHub.api_host / "search" / "code" <<? params).secure
    client.searchByUrl[GhCode](url)
  }

}

class RepoSearch private(client : Client, params : Map[String, String]) {

  def this(client : Client, q : String) = this(client, Map("q" -> q))

  def byStars() = new RepoSearch(client, params + ("order" -> "stars"))

  def byForks() = new RepoSearch(client, params + ("order" -> "forks"))

  def byUpdated() = new RepoSearch(client, params + ("order" -> "updated"))

  def ascending() = new RepoSearch(client, params + ("sort" -> "asc"))

  def search()(implicit ec : ExecutionContext) = {
    val url = (GitHub.api_host / "search" / "repositories" <<? params).secure
    client.searchByUrl[GhRepository](url)
  }

}
