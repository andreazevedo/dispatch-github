package dispatch.github

import dispatch._
import oauth._
import oauth.OAuth._

object Auth {
	val svc = GitHub.host / "login" / "oauth"
	
	def authorize_uri(client_id: String, redirect_uri: String) = 
		svc.secure / "authorize" <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri)
	
	def authorize_uri(client_id: String, redirect_uri: String, scope: List[Scope.Value]) = 
		svc.secure / "authorize" <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri, "scope" -> scope.mkString(","))
		
	def access_token(client_id: String, redirect_uri: String, client_secret: String, code: String) =
		svc.secure.POST / "access_token" <<? 
			Map("client_id" -> client_id, 
				"redirect_uri" -> redirect_uri,
				"client_secret" -> client_secret,
				"code" -> code
			) >% {
				m => m("access_token")
			}
}

object Scope extends Enumeration {
	type Scope = Value
	val user, public_repo, repo, gist = Value
}
