package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class GhIssue(number: Int, state: String, title: String, body: String, user: GhAuthor, assignee: GhAuthor, 
				   comments: Int, milestone: GhMilestone, closedAt: Date, createdAt: Date, updatedAt: Date)

case class GhMilestone(number: Int, state: String, title: String, description: String, creator: GhAuthor, 
					   openIssues: Int, closedIssues: Int, createdAt: Date, dueOn: Option[Date])


object GhIssue {
	
}