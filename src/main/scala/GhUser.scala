package dispatch.github

import dispatch._

import json._
import JsHttp._

case class GhContributor(id: Int, login: String, avatar_url: String)

case class GhUser(id: Int, login: String, name: String, email: String, avatar_url: String, account_type: String)

object GhUser {
	
	def get_authenticated_user(access_token: String) = {
		val svc = GitHub.api_host / "user"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonObj = obj(json)
			
			val id: Int = num(jsonObj.self(JsString("id"))).intValue
			val login = jsonObj.self(JsString("login")).self.toString
			val name = jsonObj.self(JsString("name")).self.toString
			val email = jsonObj.self(JsString("email")).self.toString
			val avatar_url = jsonObj.self(JsString("avatar_url")).self.toString
			val account_type = jsonObj.self(JsString("type")).self.toString
			
			GhUser(id, login, name, email, avatar_url, account_type)
		}
	}
		
}