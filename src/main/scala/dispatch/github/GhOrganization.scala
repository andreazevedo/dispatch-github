package dispatch.github

import dispatch._
import net.liftweb.json._
//import net.liftweb.json.Serialization.write

case class GhOrganization(id: Int, login: String, url: String, avatar_url: String) {
	implicit val formats = DefaultFormats

	def members(access_token:String) = {
		val svc = GitHub.api_host / "orgs" / login / "members"
		val membersJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- membersJson) yield js.extract[List[GhContributor]]
	}

	def teams(access_token:String) = GhTeam.get_teams(login, access_token)

}

object GhOrganization {
	implicit val formats = DefaultFormats
   
	def get_organizations(access_token: String) = {
		val svc = GitHub.api_host / "user" / "orgs"
		val orgJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- orgJson) yield js.extract[List[GhOrganization]]
	}

	def get_organization(org: String, access_token: String) = {
		val svc = GitHub.api_host / "orgs" / org
		val orgJson = Http(svc.secure <<? Map("access_token" -> access_token) OK as.lift.Json)
      for (js <- orgJson) yield js.extract[GhOrganization]
	}
		
}
