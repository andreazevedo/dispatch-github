package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._

class GhIssueSpec extends Specification {
	"When retrieving github issues" should {
		"return a handler" in {
			GhIssue.get_issues("andreazevedo", "dispatch-github") must haveClass[dispatch.Handler[List[GhIssue]]]
		}
	}
}
