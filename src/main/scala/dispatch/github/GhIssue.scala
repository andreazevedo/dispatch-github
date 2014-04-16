package dispatch.github

import dispatch._
import Defaults._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Date
//import java.text.SimpleDateFormat

case class GhIssueSummary(title: String, body: String, assignee: String, milestone: Option[Int], labels: List[String])

case class GhIssue(number: Int, state: String, title: String, body: String, user: GhAuthor, assignee: Option[GhAuthor], 
				   comments: Int, milestone: Option[GhMilestone], closed_at: Date, created_at: Date, updated_at: Date)


object GhIssue {
   implicit val formats = DefaultFormats
   
	def get_issues(user: String, repo: String, page: Int, state: String, perPage: Int, accessToken: String): Future[List[GhIssue]] =
		get_issues(user, repo, Map("page" -> page.toString, "state" -> state, "per_page" -> perPage.toString, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, state: String, accessToken: String): Future[List[GhIssue]] =
		get_issues(user, repo, Map("page" -> page.toString, "state" -> state, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, accessToken: String): Future[List[GhIssue]] =
		get_issues(user, repo, Map("page" -> page.toString, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, accessToken: String): Future[List[GhIssue]] =
		get_issues(user, repo, Map("access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, perPage: Int): Future[List[GhIssue]] =
		get_issues(user, repo, Map("page" -> page.toString, "per_page" -> perPage.toString))

	def get_issues(user: String, repo: String, page: Int): Future[List[GhIssue]] =
		get_issues(user, repo, Map("page" -> page.toString))

	def get_issues(user: String, repo: String, params: Map[String, String] = Map()): Future[List[GhIssue]] = {
      def get_issuesJson(user: String, repo: String) = {
         val svc = GitHub.api_host / "repos" / user / repo / "issues"
         Http((svc.secure <<? params) OK as.lift.Json)
      }
      
      for (js <- get_issuesJson(user, repo)) yield js.extract[List[GhIssue]]
	}


	def create(user: String, repo: String, issue: GhIssueSummary, accessToken: String): Future[GhIssue] =
		create(user, repo, issue, Map("access_token" -> accessToken))

	def create(user: String, repo: String, issue: GhIssueSummary, params: Map[String, String] = Map()): Future[GhIssue] = {
      def write_issuesJson(user: String, repo: String, issue: GhIssueSummary) = {
         val svc = GitHub.api_host / "repos" / user / repo / "issues"
         Http((svc.secure <<? params << write(issue)) OK as.lift.Json)
      }
		
      for (js <- write_issuesJson(user, repo, issue)) yield js.extract[GhIssue]
	}
}
