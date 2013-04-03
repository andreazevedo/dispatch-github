package dispatch.github.specs

import org.specs2.mutable._
import org.specs2.execute.Pending
import dispatch._
import dispatch.github._

class GhRepositorySpec extends Specification {
	"When retrieving github organization's repositories" should {
		"return a promise (before the request is not invoked)" in {
			//GhRepository.get_org_repos("andreazevedo") must haveClass[dispatch.Promise[List[GhRepository]]]
         Pending("Pending - not sure why we should type check this")
		}
	}

	"When retrieving github repository's branches" should {
		"return something when the branches request is invoked" in {
			val branchesRes = GhRepository.get_branches("andreazevedo", "dispatch-github")
			val branches = branchesRes()

			(branches must not beNull)
			branches.length must beGreaterThan(0)

			val branch = branches.head

			(branch.name must not beNull)
			branch.name.length must beGreaterThan(0)
			(branch.commit.sha must not beNull)
			branch.commit.sha.length must beGreaterThan(0)
			(branch.commit.url must not beNull)
			branch.commit.url.length must beGreaterThan(0)
		}
	}
}
