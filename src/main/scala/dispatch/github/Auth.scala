package dispatch.github

import dispatch._
import oauth._
//import oauth.OAuth._


abstract class Auth(val callback: String) extends oauth.Exchange
  with oauth.SomeCallback
  with oauth.SomeHttp
  with GithubConsumer
  with GithubEndpoints {
  val http = dispatch.Http
}

trait GithubEndpoints extends oauth.SomeEndpoints {
  val requestToken = "http://www.tumblr.com/oauth/request_token"
  val accessToken = "https://github.com/login/oauth/access_token"
  val authorize = "https://github.com/login/oauth/authorize"
}

// TODO
trait GithubConsumer extends oauth.SomeConsumer 

object Auth {
	val svc = GitHub.host / "login" / "oauth"
	
	def authorize_uri(client_id: String, redirect_uri: String) = 
		svc.secure / "authorize" <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri)
	
	def authorize_uri(client_id: String, redirect_uri: String, scope: List[Scope.Value]) = 
		svc.secure / "authorize" <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri, "scope" -> scope.mkString(","))
		/*
	def access_token(client_id: String, redirect_uri: String, client_secret: String, code: String) =
		svc.secure.POST / "access_token" <<? 
			Map("client_id" -> client_id, 
				"redirect_uri" -> redirect_uri,
				"client_secret" -> client_secret,
				"code" -> code
			) >% {
				m => m("access_token")
			}
         */
}

object Scope extends Enumeration {
	type Scope = Value
	val user, public_repo, repo, gist = Value
}
