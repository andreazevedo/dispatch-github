package dispatch.github

import dispatch._

import json._
import JsHttp._

case class GhOrganization(id: Int, login: String, url: String, avatar_url: String)

object GhOrganization {
	
	def get_organizations(access_token: String) = {
		val svc = GitHub.api_host / "user" / "orgs"
		svc.secure <<? Map("access_token" -> access_token) ># { json =>
			val jsonList: List[JsValue] = list(json)

			var orgs = List[GhOrganization]()

			jsonList.map { child =>
				val jsonObj = obj(child)

				val id: Int = num(jsonObj.self(JsString("id"))).intValue
				val login = jsonObj.self(JsString("login")).self.toString
				val url = jsonObj.self(JsString("url")).self.toString
				val avatar_url = jsonObj.self(JsString("avatar_url")).self.toString

				orgs = orgs :+ GhOrganization(id, login, url, avatar_url)
			}

			orgs
		}
	}
	
}


/*



*/