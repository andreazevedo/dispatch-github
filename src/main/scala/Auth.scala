package dispatch.github

import dispatch._
import oauth._
import oauth.OAuth._

object Auth {
	val svc = :/("github.com") / "login" / "oauth"
	
	def authorize_uri(client_id: String, redirect_uri: String) = 
		svc.secure / "authorize" <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri)
		
	def access_token(client_id: String, redirect_uri: String, client_secret: String, code: String) =
		svc.secure.POST / "access_token" <:< 
			Map("client_id" -> client_id, 
				"redirect_uri" -> redirect_uri,
				"client_secret" -> client_secret,
				"code" -> code
			) >% {
				m => m("access_token")
			}
}