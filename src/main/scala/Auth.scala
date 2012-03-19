package dispatch.github

import dispatch._
import oauth._
import oauth.OAuth._

object Auth {
	val svc = :/("github.com") / "login" / "oauth"
	
	def request_token(consumer: Consumer, callback_url: String) = 
		svc.secure / "authorize" <@ (consumer, callback_url) as_token
}