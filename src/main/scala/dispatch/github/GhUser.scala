package dispatch.github

import dispatch._
import net.liftweb.json._

import java.util.Date

case class GhContributor(id: Int, login: String, avatar_url: String)

case class GhAuthorSummary(name:String, date:Date, email:String)

case class GhAuthor(avatar_url: String, url: String, login: String, gravatar_id: String, id: Int)

case class GhUser(id: Int, login: String, name: String, email: String, avatar_url: String, account_type: String)


object GhUser {
	implicit val formats = DefaultFormats
   
	def get_authenticated_user(access_token: String) = {
		val svc = GitHub.api_host / "user"
		val userJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- userJson) yield js.extract[GhUser]
	}
}