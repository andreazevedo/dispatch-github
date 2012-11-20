package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Date
import java.text.SimpleDateFormat

case class GhIssueSummary(title: String, body: String, assignee: String, milestone: Option[Int], labels: List[String])

case class GhIssue(number: Int, state: String, title: String, body: String, user: GhAuthor, assignee: Option[GhAuthor], 
				   comments: Int, milestone: Option[GhMilestone], closed_at: Date, created_at: Date, updated_at: Date)


object GhIssue {
	def get_issues(user: String, repo: String, page: Int, state: String, perPage: Int, accessToken: String): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("page" -> page.toString, "state" -> state, "per_page" -> perPage.toString, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, state: String, accessToken: String): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("page" -> page.toString, "state" -> state, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, accessToken: String): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("page" -> page.toString, "access_token" -> accessToken))

	def get_issues(user: String, repo: String, accessToken: String): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("access_token" -> accessToken))

	def get_issues(user: String, repo: String, page: Int, perPage: Int): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("page" -> page.toString, "per_page" -> perPage.toString))

	def get_issues(user: String, repo: String, page: Int): Handler[List[GhIssue]] = 
		get_issues(user, repo, Map("page" -> page.toString))

	def get_issues(user: String, repo: String, params: Map[String, String] = Map()): Handler[List[GhIssue]] = {
		val svc = GitHub.api_host / "repos" / user / repo / "issues"

		svc.secure <<? params ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhIssue]]
		}
	}


	def create(user: String, repo: String, issue: GhIssueSummary, accessToken: String): Handler[GhIssue] = 
		create(user, repo, issue, Map("access_token" -> accessToken))

	def create(user: String, repo: String, issue: GhIssueSummary, params: Map[String, String] = Map()): Handler[GhIssue] = {
		val svc = GitHub.api_host / "repos" / user / repo / "issues"
		implicit val formats = DefaultFormats
		svc.secure <<? params << write(issue) ># { json =>
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[GhIssue]
		}
	}
}
