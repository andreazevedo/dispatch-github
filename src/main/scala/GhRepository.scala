package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class GhRepository(id:Int, owner_login:String, name:String, updatedAt:Date, language:String,
	html_url:String, clone_url:String, description:String, open_issues:Int)

object GhRepository {
	def get_org_repos(org_login:String, access_token:String) = {
		val svc = GitHub.api_host / "orgs" / org_login / "repos"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList: List[JsValue] = list(json)

			jsonList.map { child =>
				val jsonObj = obj(child)

				val jsonOwnerObj = obj(jsonObj.self(JsString("owner_login")))

				val id:Int = num(jsonObj.self(JsString("id"))).intValue
				val owner_login = jsonOwnerObj.self(JsString("login")).self.toString
				val name = jsonObj.self(JsString("name")).self.toString
				val updatedAt:Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObj.self(JsString("updatedAt")).self.toString)
				val language = jsonObj.self(JsString("language")).self.toString
				val html_url = jsonObj.self(JsString("html_url")).self.toString
				val clone_url = jsonObj.self(JsString("clone_url")).self.toString
				val description = jsonObj.self(JsString("description")).self.toString
				val open_issues:Int = num(jsonObj.self(JsString("open_issues"))).intValue

				GhRepository(id, owner_login, name, updatedAt, language, html_url, clone_url, description, open_issues)
			}
		}
	}
}