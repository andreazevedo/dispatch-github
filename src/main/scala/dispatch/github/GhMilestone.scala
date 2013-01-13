package dispatch.github

import dispatch._
import net.liftweb.json._
//import net.liftweb.json.Serialization.write
import java.util.Date
//import java.text.SimpleDateFormat

case class GhMilestone(number: Int, state: String, title: String, description: String, creator: GhAuthor, 
					   open_issues: Int, closed_issues: Int, created_at: Date, due_on: Option[Date])

object GhMilestone {
   implicit val formats = DefaultFormats
   
	def get_milestones(user: String, repo: String, page: Int, state: String, perPage: Int, accessToken: String): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "state" -> state, "per_page" -> perPage.toString, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, state: String, accessToken: String): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "state" -> state, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, accessToken: String): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "access_token" -> accessToken))

	def get_milestones(user: String, repo: String, accessToken: String): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("access_token" -> accessToken))

	def get_milestones(user: String, repo: String, page: Int, perPage: Int): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString, "per_page" -> perPage.toString))

	def get_milestones(user: String, repo: String, page: Int): Promise[List[GhMilestone]] = 
		get_milestones(user, repo, Map("page" -> page.toString))
		
	def get_milestones(user: String, repo: String, params: Map[String, String] = Map()): Promise[List[GhMilestone]] = {
      def get_milestonesJson(user: String, repo: String) = {
         val svc = GitHub.api_host / "repos" / user / repo / "milestones"
         Http((svc.secure <<? params) OK as.lift.Json)
      }

		for (js <- get_milestonesJson(user, repo)) yield js.extract[List[GhMilestone]]
	}
}