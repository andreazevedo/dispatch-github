package dispatch.github

import dispatch._
import oauth._
import oauth.OAuth._

object Auth {
	val svc = :/("github.com") / "login" / "oauth"
	
	def authorize_uri(client_id: String, redirect_uri: String) = 
		(svc / "authorize").secure <<? Map("client_id" -> client_id, "redirect_uri" -> redirect_uri)
}