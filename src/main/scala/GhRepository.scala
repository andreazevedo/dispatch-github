package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date

case class GhOwner(id:Int, login: String)
case class GhRepository(id:Int, owner:GhOwner, name:String, updated_at:Date, language:String,
	html_url:String, clone_url:String, description:String, open_issues:Int)

object GhRepository {
	def get_org_repos(org_login:String, access_token:String) = {
		val svc = GitHub.api_host / "orgs" / org_login / "repos"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj =>

				val jsonOwnerObj = jsonObj("owner").asObj

				val id = jsonObj("id").asInt
				val owner_id = jsonOwnerObj("id").asInt
				val owner_login = jsonOwnerObj("login").asString
				val name = jsonObj("name").asString
				val updated_at = jsonObj("updated_at").asDate
				val language = jsonObj("language").asString
				val html_url = jsonObj("html_url").asString
				val clone_url = jsonObj("clone_url").asString
				val description = jsonObj("description").asString
				val open_issues = jsonObj("open_issues").asInt

				GhRepository(id, GhOwner(owner_id, owner_login), name, updated_at, language, html_url, clone_url, description, open_issues)
			}
		}
	}
}