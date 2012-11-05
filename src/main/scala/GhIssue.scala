package dispatch.github

import dispatch._
import json._
import JsHttp._
import java.util.Date
import java.text.SimpleDateFormat

case class GhIssue(number: Int, state: String, title: String, body: String, user: GhAuthor, assignee: Option[GhAuthor], 
				   comments: Int, milestone: Option[GhMilestone], closedAt: Date, createdAt: Date, updatedAt: Date)

case class GhMilestone(number: Int, state: String, title: String, description: String, creator: GhAuthor, 
					   openIssues: Int, closedIssues: Int, createdAt: Date, dueOn: Option[Date])


object GhIssue {
	def get_issues(user: String, repo: String, params: Map[String, String] = Map()): Handler[List[GhIssue]] = {
		val svc = GitHub.api_host / "repos" / user / repo / "issues"
		svc.secure <<? params ># { json =>
			List[GhIssue]()
		}
	}
}