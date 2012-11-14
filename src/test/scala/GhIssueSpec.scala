package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._
import scalendar._

class GhIssueSpec extends Specification {
	"When retrieving github issues" should {
		"return a handler (before the request is not invoked)" in {
			GhIssue.get_issues("andreazevedo", "dispatch-github-specs") must haveClass[dispatch.Handler[List[GhIssue]]]
		}

		"return something (not null) when the issues request is invoked" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = Http(req)
			issues must notBeNull
		}

		"return at least one issue (becouse the repository has issues)" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = Http(req)
			issues.length must beGreaterThan(0)
		}

		"return at least one issue (becouse the repository has issues) with a title" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = Http(req)
			issues.first.title.size must beGreaterThan(0)
		}

		"return the right issues, containing all expected properties filled with the expected data" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = Http(req)
			val issue = issues.last

			issue must notBeNull
			issue.number must be(1)
			issue.state must equalTo("open")
			issue.title must equalTo("first issue")
			issue.body must equalTo("bla bla bla")			
			issue.user must notBeNull
			issue.user.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			issue.user.url must equalTo("https://api.github.com/users/andreazevedo")
			issue.user.login must equalTo("andreazevedo")
			issue.user.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
			issue.user.id must beEqualTo(741321)
			issue.assignee must beSome[GhAuthor]
			issue.assignee match {
				case Some(assignee) => 
					assignee.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
					assignee.url must equalTo("https://api.github.com/users/andreazevedo")
					assignee.login must equalTo("andreazevedo")
					assignee.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
					assignee.id must beEqualTo(741321)
				case _ =>
					true must be(false)
			}
			issue.comments must be(0)

			// milestone
			issue.milestone must beSome[GhMilestone]
			issue.milestone.get.number must be(1)
			issue.milestone.get.state must equalTo("open")
			issue.milestone.get.title must equalTo("first milestone")
			issue.milestone.get.description must equalTo("")
			issue.milestone.get.creator.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			issue.milestone.get.creator.url must equalTo("https://api.github.com/users/andreazevedo")
			issue.milestone.get.creator.login must equalTo("andreazevedo")
			issue.milestone.get.creator.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
			issue.milestone.get.creator.id must beEqualTo(741321)
			issue.milestone.get.open_issues must be(1)
			issue.milestone.get.closed_issues must be(0)
			issue.milestone.get.created_at must equalTo(Scalendar(year = 2012, month = 11, day = 05, hour = 18, minute = 42, second = 03).date)
			issue.milestone.get.due_on must equalTo(Scalendar(year = 2012, month = 11, day = 05, hour = 06, minute = 00, second = 00).date)

			issue.closed_at must beNull
			issue.created_at must equalTo(Scalendar(year = 2012, month = 11, day = 05, hour = 18, minute = 10, second = 02).date)
			issue.updated_at must equalTo(Scalendar(year = 2012, month = 11, day = 06, hour = 15, minute = 50, second = 53).date)
		}
	}




	"When creating a new github issues" should {
		"return a handler (before the request is not invoked)" in {
			val issue = GhIssueSummary("Test from dispatch github specs", "This is the body of the issue", "", None, Nil)
			GhIssue.create("andreazevedo", "dispatch-github-specs", issue, "fakeAccessToken") must haveClass[dispatch.Handler[GhIssue]]
		}
	}
}
