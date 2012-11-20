package dispatch.github.specs

import org.specs._
import dispatch._
import dispatch.github._

class GhMilestoneSpec extends Specification {
	"When retrieving github milestones" should {
		"return a handler (before the request is not invoked)" in {
			GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs") must haveClass[dispatch.Handler[List[GhMilestone]]]
		}

		"return something (not null) when the milestones request is invoked" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = Http(req)
			milestones must notBeNull
		}

		"return at least one milestone (becouse the repository has milestones)" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = Http(req)
			milestones.length must beGreaterThan(0)
		}

		"return at least one milestone (becouse the repository has milestones) with a non-empty title" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = Http(req)
			milestones.first.title.size must beGreaterThan(0)
		}

		"return the right milestones, containing all expected properties filled with the expected data" in {
			val req = GhMilestone.get_milestones("andreazevedo", "dispatch-github-specs")
			val milestones = Http(req)
			val milestone = milestones.last

			milestone must notBeNull
			milestone.number must be(1)
			milestone.state must equalTo("open")
			milestone.title must equalTo("first milestone")
			milestone.description must equalTo("")
			milestone.creator.avatar_url must equalTo("https://secure.gravatar.com/avatar/bcc0f1aa2a39d379e613efe4858adad3?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
			milestone.creator.url must equalTo("https://api.github.com/users/andreazevedo")
			milestone.creator.login must equalTo("andreazevedo")
			milestone.creator.gravatar_id must	equalTo("bcc0f1aa2a39d379e613efe4858adad3")
			milestone.creator.id must beEqualTo(741321)
			milestone.open_issues must be(1)
			milestone.closed_issues must be(0)
			milestone.created_at must equalTo(DateTimeHelper.createDate(2012, 11, 5, 20, 42, 3, "GMT"))
			milestone.due_on must equalTo(DateTimeHelper.createDate(2012, 11, 5, 8, 0, 0, "GMT"))
		}
	}
}