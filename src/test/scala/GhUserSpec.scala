package dispatch.github.specs

import org.specs2.mutable._
import dispatch._
import dispatch.github._

class GhUserSpec extends Specification {
    "When retrieving anonymous github user profile" should {
        "return something when the user is valid" in {
            val userRes = GhUser.get_user("juandebravo")
            val user = userRes()
            (user must not beNull)
            user.id must beEqualTo(367029)
            user.`type` must beEqualTo("User")
        }

        "return None when the user is invalid" in {
            val userRes = GhUser.get_user("juandebravoInvalidName")
            val user = userRes.completeOption
            user should be(None)
        }
    }
}
