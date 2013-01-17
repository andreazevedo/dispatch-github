package dispatch.github

import dispatch._

case class GhTree(sha:String, url:String)

case class GhCommitData(message: String, url: String, author: GhAuthorSummary, 
						committer: GhAuthorSummary, tree: GhTree, comment_count: Int)

case class GhCommitStats(total: Int, additions: Int, deletions: Int)

case class GhCommitFile(status: String, blob_url: String, patch: String, additions: Int, deletions: Int, 
						filename: String, raw_url: String, changes: Int, sha: String)

case class GhCommitSummary(commit: GhCommitData, parents: List[GhTree], url: String, sha: String, 
						   author: Option[GhAuthor], committer: Option[GhAuthor])

case class GhCommitSummaryList(commits: Map[String, GhCommitSummary])
                     
                     
case class GhCommit(stats: GhCommitStats, url: String, files: List[GhCommitFile], commit: GhCommitData, 
					committer: Option[GhAuthor], author: Option[GhAuthor], parents: List[GhTree], sha: String)


object GhCommit {
   implicit val formats = net.liftweb.json.DefaultFormats
   
   // Reminder about commit pagination . pagination will return the per_page commits starting with the one AFTER last_sha
	def get_commits(user: String, repo: String, last_sha: String, per_page: Int, access_token: String): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "per_page" -> per_page.toString, "access_token" -> access_token))

	def get_commits(user: String, repo: String, last_sha: String, access_token: String): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "access_token" -> access_token))

	def get_commits(user: String, repo: String, last_sha: String, per_page: Int): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "per_page" -> per_page.toString))

	def get_commits(user: String, repo: String, params: Map[String, String] = Map()) : Promise[List[GhCommitSummary]] = {
      def get_commitsJson(user: String, repo: String) = {
         val svc = GitHub.api_host / "repos" / user / repo / "commits"
         Http((svc.secure <<? params) OK as.lift.Json)
      }
      for (js <- get_commitsJson(user, repo)) yield js.extract[List[GhCommitSummary]]
	}

	def get_commit(user: String, repo: String, sha: String, access_token: String) = {
      def get_commitJson(user: String, repo: String, sha: String) = {
         val svc = GitHub.api_host / "repos" / user / repo / "commits" / sha
         Http((svc.secure <<? Map("access_token" -> access_token)) OK as.lift.Json)
      }
      for (js <- get_commitJson(user, repo, sha)) yield js.extract[GhCommit]
	}
}
