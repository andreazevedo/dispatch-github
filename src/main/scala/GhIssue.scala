package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import java.util.Date
import java.text.SimpleDateFormat

case class GhIssue(number: Int, state: String, title: String, body: String, user: GhAuthor, assignee: Option[GhAuthor], 
				   comments: Int, milestone: Option[GhMilestone], closed_at: Date, created_at: Date, updated_at: Date)

case class GhMilestone(number: Int, state: String, title: String, description: String, creator: GhAuthor, 
					   open_issues: Int, closed_issues: Int, created_at: Date, due_on: Date)


object GhIssue {
	def get_issues(user: String, repo: String, params: Map[String, String] = Map()): Handler[List[GhIssue]] = {
		val svc = GitHub.api_host / "repos" / user / repo / "issues"
		svc.secure <<? params ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhIssue]]
		}
	}
}