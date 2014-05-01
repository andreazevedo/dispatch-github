package dispatch.github.specs

import org.specs2.mutable._
import org.specs2.execute.Pending
import dispatch._
import dispatch.github._

class GhSearchSpec extends Specification {

  "When searching for repositories" should {
    "searching by user should return a known repository" in {
      val searchRes = GhSearch.search_repos("user:andreazevedo")
      val repos = searchRes()
      (repos must not beNull)
      (repos.find(_.name == "dispatch-github-specs") must not beEmpty)
    }
  }

  "When searching for code" should {
    "searching for the .md extension should find a known Markdown file" in {
      val searchRes = GhSearch.search_code("user:andreazevedo extension:md")
      val files = searchRes()
      (files must not beNull)
      (files.find(file => file.name == "README.md" &&
                          file.repository.name == "dispatch-github-specs")
       must not beEmpty)
    }
  }

}
