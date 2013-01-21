package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Date
import java.text.SimpleDateFormat

case class GhRepository(id:Int, owner:GhOwner, name:String, updated_at:Date, language:String,
						html_url:String, clone_url:String, description:String, open_issues:Int) {
}
case class GhBranchSummary(name: String, commit: GhCommitId)

object GhRepository {
	def get_org_repos(org: String, access_token:String) : Handler[List[GhRepository]] = 
		get_org_repos(org, Map("access_token" -> access_token))
	def get_org_repos(org: String, page: Int, per_page: Int, access_token: String) : Handler[List[GhRepository]] = 
		get_org_repos(org, Map("page" -> page.toString, "per_page" -> per_page.toString, "access_token" -> access_token))
	def get_org_repos(org:String, params:Map[String, String] = Map()) : Handler[List[GhRepository]] = {
		val svc = GitHub.api_host / "orgs" / org / "repos"
		svc.secure <<? params ># { json =>
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

	def get_branches(owner: String, repo: String, access_token:String) : Handler[List[GhBranchSummary]] =
		get_branches(owner, repo, Map("access_token" -> access_token))
	def get_branches(owner: String, repo: String, page: Int, per_page: Int, access_token: String) : Handler[List[GhBranchSummary]] = 
		get_branches(owner, repo: String, Map("page" -> page.toString, "per_page" -> per_page.toString, "access_token" -> access_token))
	def get_branches(owner: String, repo: String, params:Map[String, String] = Map()) : Handler[List[GhBranchSummary]] = {
		val svc = GitHub.api_host / "repos" / owner / repo / "branches"
		svc.secure <<? params ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhBranchSummary]]
		}
	}
}