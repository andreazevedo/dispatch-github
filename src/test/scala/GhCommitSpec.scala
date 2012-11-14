package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._

class GhCommitSpec extends Specification {
	"When retrieving github commits" should {
		"return a handler" in {
			GhCommit.get_commits("andreazevedo", "dispatch-github") must haveClass[dispatch.Handler[List[GhCommitSummary]]]
		}

		"return something (not null) when the commits request is invoked" in {
			val req = GhCommit.get_commits("andreazevedo", "dispatch-github")
			val commits = Http(req)
			commits must notBeNull
		}

		"return the right commit(s) when the commits request is invoked passing last_sha" in {
			val req = GhCommit.get_commits("andreazevedo", "dispatch-github", "d77212a4a10c19e1329bd2614fc76abfcb15732d", 30)
			val commits = Http(req)
			commits must notBeNull
			commits.length must notBe(0)
			commits(0).sha must equalTo("7433b3d714324418aca12aa6b04dbd21154bcc07")
			commits(0).author must haveClass[Some[GhAuthor]]
		}

		"return the right commits, containing all expected properties filled with the expected data" in {
			val req = GhCommit.get_commits("andreazevedo", "dispatch-github", "7433b3d714324418aca12aa6b04dbd21154bcc07", 30)
			val commits = Http(req)

			val commit = commits.first

			commit must notBeNull
			commit.commit must notBeNull
			commit.commit.message must equalTo("Started issues integration")
			commit.commit.url must equalTo("https://api.github.com/repos/andreazevedo/dispatch-github/git/commits/98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.commit.author.name must equalTo("Andre Azevedo")
			commit.commit.author.date must equalTo(DateTimeHelper.createDate(2012, 4, 19, 4, 23, 7, "GMT"))
			commit.commit.author.email must equalTo("andre.azevedo@gmail.com")
			commit.commit.committer.name must equalTo("Andre Azevedo")
			commit.commit.committer.date must equalTo(DateTimeHelper.createDate(2012, 4, 19, 4, 23, 7, "GMT"))
			commit.commit.committer.email must equalTo("andre.azevedo@gmail.com")
			commit.commit.tree.sha must equalTo("011225168ca6514959e833b22cf0a1d287dfb1a1")
			commit.commit.tree.url must equalTo("https://api.github.com/repos/andreazevedo/dispatch-github/git/trees/011225168ca6514959e833b22cf0a1d287dfb1a1")
			commit.parents.length must be(1)
			commit.parents(0).sha must equalTo("f1346bb6cec1146ad923398a20dcf7f3aae04541")
			commit.parents(0).url must equalTo("https://api.github.com/repos/andreazevedo/dispatch-github/commits/f1346bb6cec1146ad923398a20dcf7f3aae04541")
			commit.url must equalTo("https://api.github.com/repos/andreazevedo/dispatch-github/commits/98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.sha must equalTo("98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.author must beSome[GhAuthor]
			commit.author match {
				case Some(author) => 
					author.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
					author.url must equalTo("https://api.github.com/users/andreazevedo")
					author.login must equalTo("andreazevedo")
					author.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
					author.id must beEqualTo(741321)
				case _ =>
					true must be(false)
			}
			commit.committer must beSome[GhAuthor]
			commit.committer match {
				case Some(committer) => 
					committer.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
					committer.url must equalTo("https://api.github.com/users/andreazevedo")
					committer.login must equalTo("andreazevedo")
					committer.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
					committer.id must beEqualTo(741321)
				case _ =>
					true must be(false)
			}

			true
		}
	}
}
