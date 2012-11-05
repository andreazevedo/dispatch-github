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
}
