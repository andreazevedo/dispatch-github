package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._

class GhRepositorySpec extends Specification {
	"When retrieving github organization's repositories" should {
		"return a handler (before the request is not invoked)" in {
			GhRepository.get_org_repos("andreazevedo") must haveClass[dispatch.Handler[List[GhRepository]]]
		}
	}

	"When retrieving github repository's branches" should {
		"return something when the branches request is invoked" in {
			val req = GhRepository.get_branches("andreazevedo", "dispatch-github")
			val branches = Http(req)
			branches must notBeNull

			branches.length must beGreaterThan(0)

			val branch = branches.first

			branch.name must notBeNull
			branch.name.length must beGreaterThan(0)
			branch.commit.sha must notBeNull
			branch.commit.sha.length must beGreaterThan(0)
			branch.commit.url must notBeNull
			branch.commit.url.length must beGreaterThan(0)
		}
	}
}
