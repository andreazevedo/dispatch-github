package dispatch.github

import dispatch._
import net.liftweb.json._

case class GhTeamSummary(url: String, name: String, id: Int) {
	def members(access_token: String) = GhTeam.get_team_members(id, access_token)
	def repositories(access_token: String) = GhTeam.get_team_repositories(id, access_token)
}

case class GhTeam(url: String, name: String, id: Int, permission: String, members_count: Int, repos_count: Int) {
	def members(access_token: String) = GhTeam.get_team_members(id, access_token)
	def repositories(access_token: String) = GhTeam.get_team_repositories(id, access_token)
}

object GhTeam {
	implicit val formats = DefaultFormats

	def get_teams(org: String, access_token: String) = {
		val svc = GitHub.api_host / "orgs" / org / "teams"
		val teamsJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- teamsJson) yield List(js.extract[GhTeamSummary])
	}

	def get_team(id: Int, access_token: String) = {
		val svc = GitHub.api_host / "teams" / id.toString
		val teamsJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- teamsJson) yield js.extract[GhTeam]
	}

	def get_team_members(id: Int, access_token: String) = {
		val svc = GitHub.api_host / "teams" / id.toString / "members"
		val teamsJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- teamsJson) yield List(js.extract[GhAuthor])
	}

	def get_team_repositories(id: Int, access_token: String) = {
		val svc = GitHub.api_host / "teams" / id.toString / "repos"
		val teamsJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- teamsJson) yield List(js.extract[GhRepository])
	}
}