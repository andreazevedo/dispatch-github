package dispatch.github.specs

import org.specs2.mutable._
import dispatch._
import dispatch.github._
import java.util.Date

class GhIssueSpec extends Specification {
	"When retrieving github issues" should {
		"return something (not null) when the issues request is invoked" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = req()
			issues must not beNull
		}

		"return at least one issue (becouse the repository has issues)" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = req()
			issues.length must beGreaterThan(0)
		}

		"return at least one issue (becouse the repository has issues) with a non-empty title" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = req()
			issues.head.title.size must beGreaterThan(0)
		}

		"return the right issues, containing all expected properties filled with the expected data" in {
			val req = GhIssue.get_issues("andreazevedo", "dispatch-github-specs")
			val issues = req()
			val issue = issues.last

			(issue must not beNull)
			issue.number must beEqualTo(1) 
			issue.state must beEqualTo("open")
			issue.title must beEqualTo("first issue")
			issue.body must beEqualTo("bla bla bla")			
			(issue.user must not beNull)
			issue.user.avatar_url must beEqualTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			issue.user.url must beEqualTo("https://api.github.com/users/andreazevedo")
			issue.user.login must beEqualTo("andreazevedo")
			issue.user.gravatar_id must beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
			issue.user.id must beEqualTo(741321)
			issue.assignee must beSome[GhAuthor]
			issue.assignee match {
				case Some(assignee) => 
					assignee.avatar_url must beEqualTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
					assignee.url must beEqualTo("https://api.github.com/users/andreazevedo")
					assignee.login must beEqualTo("andreazevedo")
					assignee.gravatar_id must	beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
					assignee.id must beEqualTo(741321)
				case _ =>
					failure //true must be(false)
			}
			issue.comments must beEqualTo(0)

			// milestone
			issue.milestone must beSome[GhMilestone]
			issue.milestone.get.number must beEqualTo(1)
			issue.milestone.get.state must beEqualTo("open")
			issue.milestone.get.title must beEqualTo("first milestone")
			issue.milestone.get.description must beEqualTo("")
			issue.milestone.get.creator.avatar_url must beEqualTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			issue.milestone.get.creator.url must beEqualTo("https://api.github.com/users/andreazevedo")
			issue.milestone.get.creator.login must beEqualTo("andreazevedo")
			issue.milestone.get.creator.gravatar_id must	beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
			issue.milestone.get.creator.id must beEqualTo(741321)
			issue.milestone.get.open_issues must beEqualTo(1)
			issue.milestone.get.closed_issues must beEqualTo(0)
			issue.milestone.get.created_at must beEqualTo(DateTimeHelper.createDate(2012, 11, 5, 20, 42, 3, "GMT"))
			issue.milestone.get.due_on must beSome[Date]
			issue.milestone.get.due_on.get must beEqualTo(DateTimeHelper.createDate(2012, 11, 5, 8, 0, 0, "GMT"))

			issue.closed_at must beNull
			issue.created_at must beEqualTo(DateTimeHelper.createDate(2012, 11, 5, 20, 10, 2, "GMT"))
			issue.updated_at must beEqualTo(DateTimeHelper.createDate(2012, 11, 6, 17, 50, 53, "GMT"))
		}
	}



   // TODO issue creation test
	
   "When creating a new github issues" should {
		"return a promise (before the request is not invoked)" in {
			val issue = GhIssueSummary("Test from dispatch github specs", "This is the body of the issue", "", None, Nil)
			//GhIssue.create("andreazevedo", "dispatch-github-specs", issue, "fakeAccessToken") must haveClass[dispatch.Promise[GhIssue]]
         pending
		}
	}
}
