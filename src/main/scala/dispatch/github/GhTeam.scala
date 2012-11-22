package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

case class GhTeamSummary(url: String, name: String, id: Int) {
	def members(access_token: String) = GhTeam.get_team_members(id, access_token)
}

case class GhTeam(url: String, name: String, id: Int, permission: String, members_count: Int, repos_count: Int) {
	def members(access_token: String) = GhTeam.get_team_members(id, access_token)
}

object GhTeam {
	def get_teams(org: String, access_token: String) = {
		val svc = GitHub.api_host / "orgs" / org / "teams"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhTeamSummary]]
		}
	}

	def get_team(id: Int, access_token: String) = {
		val svc = GitHub.api_host / "teams" / id.toString
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[GhTeam]
		}
	}

	def get_team_members(id: Int, access_token: String)= {
		val svc = GitHub.api_host / "teams" / id.toString / "members"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhAuthor]]
		}
	}
}