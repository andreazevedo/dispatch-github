package dispatch.github.specs

import org.specs._

import dispatch._
import dispatch.github._

class GhCommitSpec extends Specification {
	"When retrieving github commits" should {
		"return a handler" in {
			GhCommit.get_commits("andreazevedo", "dispatch-github") must haveClass[dispatch.Handler[List[GhCommitSummary]]]
		}

		"return something (not null) when the request is invoked" in {
			val req = GhCommit.get_commits("andreazevedo", "dispatch-github")
			val commits = Http(req)
			commits must notBeNull
		}

		"return the right commit(s) when the request is invoked passing last_sha" in {
			val req = GhCommit.get_commits("andreazevedo", "dispatch-github", "d77212a4a10c19e1329bd2614fc76abfcb15732d", 30)
			val commits = Http(req)
			commits must notBeNull
			commits.length must notBe(0)
			commits(0).sha must equalTo("7433b3d714324418aca12aa6b04dbd21154bcc07")
			commits(0).author must haveClass[Some[GhAuthor]]
		}
	}
}
