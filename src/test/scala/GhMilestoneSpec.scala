package dispatch.github.specs

import org.specs2.mutable._
import org.specs2.execute.Pending
import dispatch._
import dispatch.github._
import java.util.Date

class GhMilestoneSpec extends Specification {
	"When retrieving github milestones" should {
		"return something (not null) when the milestones request is invoked" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = req()
			milestones must not beNull
		}

		"return at least one milestone (becouse the repository has milestones)" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = req()
			milestones.length must beGreaterThan(0)
		}

		"return at least one milestone (becouse the repository has milestones) with a non-empty title" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = req()
			milestones.head.title.size must beGreaterThan(0)
		}

		"return the right milestones, containing all expected properties filled with the expected data" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = req()
			val milestone = milestones.last

			(milestone must not beNull)
			milestone.number must beEqualTo(1)
			milestone.state must beEqualTo("open")
			milestone.title must beEqualTo("first milestone")
			milestone.description must beEqualTo("")
			milestone.creator.avatar_url must beEqualTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			milestone.creator.url must beEqualTo("https://api.github.com/users/andreazevedo")
			milestone.creator.login must beEqualTo("andreazevedo")
			milestone.creator.gravatar_id must	beEqualTo("bcc0f1aa2a39d379e613efe4858adad3")
			milestone.creator.id must beEqualTo(741321)
			milestone.open_issues must beEqualTo(1)
			milestone.closed_issues must beEqualTo(0)
			milestone.created_at must beEqualTo(DateTimeHelper.createDate(2012, 11, 5, 20, 42, 3, "GMT"))
			milestone.due_on must beSome[Date]
			milestone.due_on.get must beEqualTo(DateTimeHelper.createDate(2012, 11, 5, 8, 0, 0, "GMT"))
		}
	}
}