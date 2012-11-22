package dispatch.github

import dispatch._
import json._
import JsHttp._

case class GhOrganization(id: Int, login: String, url: String, avatar_url: String) {

	def members(access_token:String) = {
		val svc = GitHub.api_host / "orgs" / login / "members"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj =>
				val id: Int = jsonObj("id").asInt
				val login: String = jsonObj("login").asString
				val avatar_url = jsonObj("avatar_url").asString

				GhContributor(id, login, avatar_url)
			}
		}
	}

	def teams(access_token:String) = GhTeam.get_teams(login, access_token)

}

object GhOrganization {
	
	def get_organizations(access_token: String) = {
		val svc = GitHub.api_host / "user" / "orgs"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj =>
				
				val id: Int = jsonObj("id").asInt
				val login = jsonObj("login").asString
				val url = jsonObj("url").asString
				val avatar_url = jsonObj("avatar_url").asString

				GhOrganization(id, login, url, avatar_url)
			}
		}
	}
		
}
