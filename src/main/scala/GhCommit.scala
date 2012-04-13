package dispatch.github

import dispatch._

import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class GhAuthor(name:String, date:Date, email:String)
case class GhTree(sha:String, url:String)

case class GhCommit(sha:String, url:String, author:GhAuthor, committer:GhAuthor, 
					message:String, tree:GhTree, parents:List[GhTree])


object GhCommit {
	def get_commits(user:String, repo:String, access_token:String) = {
		val svc = GitHub.api_host / "repos" / user / repo / "commits"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList = parse.jsonList(json)

			jsonList.map { jsonObj =>

				val sha = jsonObj("sha").asString
				val url = jsonObj("url").asString
				val author = parseAuthor(jsonObj("author").asObj)
				val committer = parseAuthor(jsonObj("committer").asObj)
				val message = jsonObj("message").asString
				val tree = parseTree(jsonObj("tree").asObj)

				val parents = jsonObj("parents").asList.map { jsonParentObj =>
					parseTree(jsonParentObj)
				}

				GhCommit(sha, url, author, committer, message, tree, parents)
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