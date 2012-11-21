package dispatch.github

import dispatch._
import json.JsHttp._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Date
import java.text.SimpleDateFormat

case class GhMilestone(number: Int, state: String, title: String, description: String, creator: GhAuthor, 
					   open_issues: Int, closed_issues: Int, created_at: Date, due_on: Option[Date])

object GhMilestone {
	def get_milestones(user: String, repo: String, page: Int, state: String, perPage: Int, accessToken: String): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "state" -> state, "per_page" -> perPage.toString, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, state: String, accessToken: String): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "state" -> state, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, accessToken: String): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, accessToken: String): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, perPage: Int): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "per_page" -> perPage.toString))

	def get_milestones(user: String, repo: String, page: Int): Handler[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString))
		
	def get_milestones(user: String, repo: String, params: Map[String, String] = Map()): Handler[List[GhMilestone]] = {
		val svc = GitHub.api_host / "repos" / user / repo / "milestones"

		svc.secure <<? params ># { json =>
			implicit val formats = DefaultFormats
			val jsonObj = JsonParser.parse(json.toString)
			jsonObj.extract[List[GhMilestone]]
		}
	}
}