package dispatch.github.specs

import org.specs2.mutable._
import org.specs2.execute.Pending
import dispatch._
import dispatch.github._

class GhCommitSpec extends Specification {
	"When retrieving github commits" should {
		"return something (not null) when the commits request is invoked" in {
         val commitsRes = GhCommit.get_commits("andreazevedo", "dispatch-github", "d77212a4a10c19e1329bd2614fc76abfcb15732d", 30)
			val commits = commitsRes()
         commits must not beNull
		}

		"return the right commit(s) when the commits request is invoked passing last_sha" in {
			val commitsRes = GhCommit.get_commits("andreazevedo", "dispatch-github", "d77212a4a10c19e1329bd2614fc76abfcb15732d", 30 )
         val commits = commitsRes()

         (commits must not beNull)  
         commits.length mustNotEqual 0
         commits(0).sha must beEqualTo("7433b3d714324418aca12aa6b04dbd21154bcc07")
         commits(0).author must haveClass[Some[GhAuthor]]
		}

      // TODO : check that getCommits.head and getCommit return the same results
		"return the right commits, containing all expected properties filled with the expected data" in {
			val commitsRes = GhCommit.get_commits("andreazevedo", "dispatch-github", "7433b3d714324418aca12aa6b04dbd21154bcc07", 30)
			val commits = commitsRes()
			val commit = commits.head

			(commit must not beNull)
			(commit.commit must not beNull)
			commit.commit.message must beEqualTo("Started issues integration")
			commit.commit.url must beEqualTo("https://api.github.com/repos/andreazevedo/dispatch-github/git/commits/98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.commit.author.name must beEqualTo("Andre Azevedo")
			commit.commit.author.date must beEqualTo(DateTimeHelper.createDate(2012, 4, 19, 4, 23, 7, "GMT"))
			commit.commit.author.email must beEqualTo("andre.azevedo@gmail.com")
			commit.commit.committer.name must beEqualTo("Andre Azevedo")
			commit.commit.committer.date must beEqualTo(DateTimeHelper.createDate(2012, 4, 19, 4, 23, 7, "GMT"))
			commit.commit.committer.email must beEqualTo("andre.azevedo@gmail.com")
			commit.commit.tree.sha must beEqualTo("011225168ca6514959e833b22cf0a1d287dfb1a1")
			commit.commit.tree.url must beEqualTo("https://api.github.com/repos/andreazevedo/dispatch-github/git/trees/011225168ca6514959e833b22cf0a1d287dfb1a1")
			commit.parents.length must beEqualTo(1)
			commit.parents(0).sha must beEqualTo("f1346bb6cec1146ad923398a20dcf7f3aae04541")
			commit.parents(0).url must beEqualTo("https://api.github.com/repos/andreazevedo/dispatch-github/commits/f1346bb6cec1146ad923398a20dcf7f3aae04541")
			commit.url must beEqualTo("https://api.github.com/repos/andreazevedo/dispatch-github/commits/98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.sha must beEqualTo("98b8f958e8a74af02e9b0e7e1a641e029c88b1f8")
			commit.author must beSome[GhAuthor]
			commit.author match {
				case Some(author) => 
					author.avatar_url must beEqualTo("https://avatars.githubusercontent.com/u/741321?")
					author.url must beEqualTo("https://api.github.com/users/andreazevedo")
					author.login must beEqualTo("andreazevedo")
					author.gravatar_id must	beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
					author.id must beEqualTo(741321)
				case _ =>
					failure
			}
			commit.committer must beSome[GhAuthor]
			commit.committer match {
				case Some(committer) => 
					committer.avatar_url must beEqualTo("https://avatars.githubusercontent.com/u/741321?")
					committer.url must beEqualTo("https://api.github.com/users/andreazevedo")
					committer.login must beEqualTo("andreazevedo")
					committer.gravatar_id must	beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
					committer.id must beEqualTo(741321)
				case _ =>
					failure
			}

			true
		}
	}
}
