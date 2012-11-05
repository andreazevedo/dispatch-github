package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._

class GhIssueSpec extends Specification {
	"When retrieving github issues" should {
		"return a handler" in {
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
			issues(0).title.size must beGreaterThan(0)
		}
	}
}
