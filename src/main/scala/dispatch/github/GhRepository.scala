package dispatch.github

import dispatch._
import net.liftweb.json._
import java.util.Date
import java.text.SimpleDateFormat

case class GhRSimpleRepository(id: Int, owner: GhOwner, name: String, html_url: String, description: String)

case class GhRepository(id: Int, owner: GhOwner, name: String, updated_at: Date, language: String,
	html_url: String, clone_url: String, description: String, open_issues: Int)
  
case class GhBranchSummary(name: String, commit: GhCommitId)
   
object GhRepository {
   implicit val formats = DefaultFormats
   
	def get_org_repos(org: String, access_token:String) : Promise[List[GhRepository]] = 
		get_org_repos(org, Map("access_token" -> access_token))

	def get_org_repos(org: String, page: Int, per_page: Int, access_token: String) : Promise[List[GhRepository]] = 
		get_org_repos(org, Map("page" -> page.toString, "per_page" -> per_page.toString, "access_token" -> access_token))

	def get_org_repos(org:String, params:Map[String, String] = Map()) : Promise[List[GhRepository]] = {
		val svc = GitHub.api_host / "orgs" / org / "repos"
		val reposJson = Http(svc.secure <<? params OK as.lift.Json) 
		for (js <- reposJson) yield js.extract[List[GhRepository]]
	}

	def get_all_repos(access_token:String) : Promise[List[GhRSimpleRepository]] = 
		get_all_repos(Map("access_token" -> access_token))
   
	def get_all_repos(page: Int, per_page: Int, access_token: String) : Promise[List[GhRSimpleRepository]] = 
		get_all_repos(Map("page" -> page.toString, "per_page" -> per_page.toString, "access_token" -> access_token))

	def get_all_repos(params:Map[String, String] = Map()) : Promise[List[GhRSimpleRepository]] = {
		val svc = GitHub.api_host / "repositories"
		val reposJson = Http(svc.secure <<? params OK as.lift.Json) 
		for (js <- reposJson) yield js.extract[List[GhRSimpleRepository]]
	}

	def get_branches(owner: String, repo: String, access_token:String) : Promise[List[GhBranchSummary]] =
		get_branches(owner, repo, Map("access_token" -> access_token))

   def get_branches(owner: String, repo: String, page: Int, per_page: Int, access_token: String) : Promise[List[GhBranchSummary]] = 
		get_branches(owner, repo: String, Map("page" -> page.toString, "per_page" -> per_page.toString, "access_token" -> access_token))
	
   def get_branches(owner: String, repo: String, params:Map[String, String] = Map()) : Promise[List[GhBranchSummary]] = {
		val svc = GitHub.api_host / "repos" / owner / repo / "branches"
		val branchesJson = Http(svc.secure <<? params OK as.lift.Json) 
		for (js <- branchesJson) yield js.extract[List[GhBranchSummary]]
	}
}