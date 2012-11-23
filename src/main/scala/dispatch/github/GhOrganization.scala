package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

case class GhOrganization(id: Int, login: String, url: String, avatar_url: String) {

	def members(access_token:String) = {
		val svc = GitHub.api_host / "orgs" / login / "members"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhContributor]]
		}
	}

	def teams(access_token:String) = GhTeam.get_teams(login, access_token)

}

object GhOrganization {
	
	def get_organizations(access_token: String) = {
		val svc = GitHub.api_host / "user" / "orgs"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhOrganization]]
		}
	}

	def get_organization(org: String, access_token: String) = {
		val svc = GitHub.api_host / "orgs" / org
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[GhOrganization]
		}
	}

		
}
