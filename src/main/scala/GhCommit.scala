package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class GhAuthorSummary(name:String, date:Date, email:String)
case class GhAuthor(avatar_url: String, url: String, login: String, gravatar_id: String, id: Int)

case class GhTree(sha:String, url:String)

case class GhCommitData(message: String, url: String, author: GhAuthorSummary, 
						committer: GhAuthorSummary, tree: GhTree)

case class GhCommitStats(total: Int, additions: Int, deletions: Int)

case class GhCommitFile(status: String, blob_url: String, patch: String, additions: Int, deletions: Int, 
						filename: String, raw_url: String, change: Int, sha: String)

case class GhCommitSummary(commit: GhCommitData, parents: List[GhTree], url: String, sha: String, 
						   author: GhAuthor, committer: GhAuthor)

case class GhCommit(stats: GhCommitStats, url: String, files: List[GhCommitFile], commit: GhCommitData, 
					committer: GhAuthor, author: GhAuthor, parents: List[GhTree], sha: String)


object GhCommit {

	def get_commits(user: String, repo: String, access_token: String, last_sha: String, per_page: Int): Handler[List[GhCommitSummary]] =
		get_commits(user, repo, access_token, Map("last_sha" -> last_sha, "per_page" -> per_page.toString))

	def get_commits(user: String, repo: String, access_token: String, params: Map[String, String] = Map()): Handler[List[GhCommitSummary]] = {
		val svc = GitHub.api_host / "repos" / user / repo / "commits"
		svc.secure <<? (params + ("access_token" -> access_token)) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj => 
				parseCommitSummary(jsonObj)
			}
		}
	}

	def get_commit(user: String, repo: String, sha: String, access_token: String) = {
		val svc = GitHub.api_host / "repos" / user / repo / "commits" / sha
		svc.secure <<? Map("access_token" -> access_token) ># { json => 
			val jsonObj = parse.jsonObj(json)

			parseCommit(jsonObj)
		}
	}

	private	def parseCommit(jsonObj: JsonObject) = {
		val stats = parseStats(jsonObj("stats").asObj)
		val url = jsonObj("url").asString
		val files = jsonObj("files").asList.map { jsonParentObj => 
			parseFile(jsonParentObj)
		}
		val commit = parseCommitData(jsonObj("commit").asObj)
		val committer = parseAuthor(jsonObj("committer").asObj)
		val author = parseAuthor(jsonObj("author").asObj)
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
		val change = jsonObj("change").asInt
		val sha = jsonObj("sha").asString

		GhCommitFile(status, blob_url, patch, additions, deletions, filename, raw_url, change, sha)
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
		val committer = parseAuthor(jsonObj("committer").asObj)
		val author = parseAuthor(jsonObj("author").asObj)
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
}