package dispatch.github

import dispatch._
//import dispatch.as.lift
//import net.liftweb.json.{ JsonParser, JValue }

//import JsHttp._

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


/*
class liftWrapper(jsonObj: JValue) 

object liftWrapper {
   def apply(s: String) = jsonObj.extract[GhCommit]
}

object parseCommit extends (Response => GhCommit) {
  def apply(r: Response) =
    (dispatch.as.String andThen parse)(r)

  def parse(s: String) = {
      val liftwrapper = new liftWrapper(JsonParser.parse(s))
      liftwrapper(s)
  }
}
*/

object GhCommit {
   implicit val formats = net.liftweb.json.DefaultFormats
   
	def get_commits(user: String, repo: String, last_sha: String, per_page: Int, access_token: String): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "per_page" -> per_page.toString, "access_token" -> access_token))

	def get_commits(user: String, repo: String, last_sha: String, access_token: String): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "access_token" -> access_token))

	def get_commits(user: String, repo: String, last_sha: String, per_page: Int): Promise[List[GhCommitSummary]] =
		get_commits(user, repo, Map("last_sha" -> last_sha, "per_page" -> per_page.toString))

	def get_commits(user: String, repo: String, params: Map[String, String] = Map()) : Promise[List[GhCommitSummary]] = {
      def get_commitsJson(user: String, repo: String) = {
         val svc = GitHub.api_host / "repos" / user / repo / "commits"
         //Http((svc.secure <<? params) OK as.lift.Json)
         Http((svc.secure <<? params) OK as.lift.Json)
      }
		//val seq = for (js <- get_commitsJson(user, repo)) yield js.extract[GhCommitSummary]
		//seq.head
      for (js <- get_commitsJson(user, repo)) yield js.extract[List[GhCommitSummary]]
	}

	def get_commit(user: String, repo: String, sha: String, access_token: String) = {
      def get_commitJson(user: String, repo: String, sha: String) = {
         //def svc = GitHub.api_host.secure / "repos" / user / repo / "commits" / sha
         //def svcp = svc <<? Map("access_token" -> access_token)
         //Http(svcp OK as.lift.Json)
         val svc = GitHub.api_host / "repos" / user / repo / "commits" / sha
         Http((svc.secure <<? Map("access_token" -> access_token)) OK as.lift.Json)
      }
		// val seq = for (js <- get_commitJson(user, repo, sha)) yield js.extract[GhCommit]
		// seq.head
      for (js <- get_commitJson(user, repo, sha)) yield js.extract[GhCommit]
	}
/*
	private	def parseCommit(jsonObj: JsonObject) = {
		val stats = parseStats(jsonObj("stats").asObj)
		val url = jsonObj("url").asString
		val files = jsonObj("files").asList.map { jsonParentObj => 
			parseFile(jsonParentObj)
		}
		val commit = parseCommitData(jsonObj("commit").asObj)
		val committer = if (jsonObj.contains("committer")) Some(parseAuthor(jsonObj("committer").asObj)) else None
		val author = if (jsonObj.contains("author")) Some(parseAuthor(jsonObj("author").asObj)) else None
		val parents = jsonObj("parents").asList.map { jsonParentObj => 
			parseTree(jsonParentObj)
		}
		val sha = jsonObj("sha").asString

		GhCommit(stats, url, files, commit, committer, author, parents, sha)
	}

	private def parseFile(jsonObj: JsonObject) = {
		val status = jsonObj("status").asString
		val blob_url = jsonObj("blob_url").asString
		val patch = if (jsonObj.contains("patch")) jsonObj("patch").asString else ""
		val additions = jsonObj("additions").asInt
		val deletions = jsonObj("deletions").asInt
		val filename = jsonObj("filename").asString
		val raw_url = jsonObj("raw_url").asString
		val changes = if (jsonObj.contains("changes")) jsonObj("changes").asInt else 0
		val sha = jsonObj("sha").asString

		GhCommitFile(status, blob_url, patch, additions, deletions, filename, raw_url, changes, sha)
	}

	private def parseStats(jsonObj: JsonObject) = {
		val total = jsonObj("total").asInt
		val additions = jsonObj("additions").asInt
		val deletions = jsonObj("deletions").asInt

		GhCommitStats(total, additions, deletions)
	}

	private def parseCommitSummary(jsonObj: JsonObject) = {
		val url = jsonObj("url").asString
		val commit = parseCommitData(jsonObj("commit").asObj)
		val committer = if (jsonObj.contains("committer")) Some(parseAuthor(jsonObj("committer").asObj)) else None
		val author = if (jsonObj.contains("author")) Some(parseAuthor(jsonObj("author").asObj)) else None
		val parents = jsonObj("parents").asList.map { jsonParentObj => 
			parseTree(jsonParentObj)
		}
		val sha = jsonObj("sha").asString

		GhCommitSummary(commit, parents, url, sha, author, committer)
	}

	private def parseCommitData(jsonObj: JsonObject) = {
		val message = jsonObj("message").asString
		val url = jsonObj("url").asString
		val committer = parseAuthorSummary(jsonObj("committer").asObj)
		val author = parseAuthorSummary(jsonObj("author").asObj)
		val tree = parseTree(jsonObj("tree").asObj)

		GhCommitData(message, url, author, committer, tree)
	}

	private def parseAuthor(jsonObj: JsonObject) = {
		val avatar_url = jsonObj("avatar_url").asString
		val url = jsonObj("url").asString
		val login = jsonObj("login").asString
		val gravatar_id = jsonObj("gravatar_id").asString
		val id = jsonObj("id").asInt

		GhAuthor(avatar_url, url, login, gravatar_id, id)
	}

	private def parseAuthorSummary(jsonObj: JsonObject) = {
		val name = jsonObj("name").asString
		val date = jsonObj("date").asDate
		val email = jsonObj("email").asString

		GhAuthorSummary(name, date, email)
	}

	private def parseTree(jsonObj: JsonObject) = { 
   
   
		val sha = jsonObj("sha").asString
		val url = jsonObj("url").asString

		GhTree(sha, url)
	}

*/   
}
