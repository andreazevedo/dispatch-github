package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class GhAuthor(name:String, date:Date, email:String)
case class GhTree(sha:String, url:String)

case class GhCommitSummary(sha:String, url:String, author:GhAuthor, committer:GhAuthor, 
					message:String, tree:GhTree, parents:List[GhTree])


object GhCommit {
	def get_commits(user:String, repo:String, access_token:String) = {
		val svc = GitHub.api_host / "repos" / user / repo / "commits"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj =>
				val sha = jsonObj("sha").asString
				val parents = jsonObj("parents").asList.map { jsonParentObj =>
					parseTree(jsonParentObj)
				}
				
				val jsonCommitObj = jsonObj("commit").asObj /* hacking as github documentation is incorrect */
				
				val url = jsonCommitObj("url").asString
				val author = parseAuthor(jsonCommitObj("author").asObj)
				val committer = parseAuthor(jsonCommitObj("committer").asObj)
				val message = jsonCommitObj("message").asString
				val tree = parseTree(jsonCommitObj("tree").asObj)

				GhCommitSummary(sha, url, author, committer, message, tree, parents)
			}
		}
	}

	private def parseAuthor(jsonObj: JsonObject) = {
		val name = jsonObj("name").asString
		val date = jsonObj("date").asDate
		val email = jsonObj("email").asString

		GhAuthor(name, date, email)
	}

	private def parseTree(jsonObj: JsonObject) = {
		val sha = jsonObj("sha").asString
		val url = jsonObj("url").asString

		GhTree(sha, url)
	}
}