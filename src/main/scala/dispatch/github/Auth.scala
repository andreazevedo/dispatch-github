package dispatch.github

import dispatch._
import oauth._
import com.ning.http.client.oauth._

class AuthExchange(val callback: String, val consumer: ConsumerKey) extends oauth.Exchange
  with oauth.SomeCallback
  with oauth.SomeHttp
  with oauth.SomeConsumer
  with GithubEndpoints {
  val http = dispatch.Http
}

trait GithubEndpoints extends oauth.SomeEndpoints {
  val requestToken = "https://github.com/login/oauth/request_token"
  val accessToken = "https://github.com/login/oauth/access_token"
  val authorize = "https://github.com/login/oauth/authorize"
}


object Auth {
	val svc = GitHub.host / "login" / "oauth"

   def message[A](promised: Promise[A], ctx: String) =
      for (exc <- promised.either.left)
      yield "Unexpected problem fetching %s:\n%s".format(ctx, exc.getMessage)

	def webapp_authorize_uri(client_id: String, redirect_uri: String) = {
		authorize_uri( Map("client_id" -> client_id, "redirect_uri" -> redirect_uri))
   }
	
	def webapp_authorize_uri(client_id: String, redirect_uri: String, scope: List[Scope.Value]) = {
		authorize_uri(Map("client_id" -> client_id, "redirect_uri" -> redirect_uri, "scope" -> scope.mkString(",")))
   }

	def authorize_uri(params: Map[String, String] = Map()) = {
		val response = Http(svc.secure / "authorize" <<? params)
		for (r <- response) yield r.getHeaders.getFirstValue("Location")
   }

   
	def access_token(client_id: String, redirect_uri: String, client_secret: String, code: String) = {
		var tokenResponse = Http ( svc.secure.POST / "access_token" <<? 
                                    Map("client_id" -> client_id, 
                                    "redirect_uri" -> redirect_uri,
                                    "client_secret" -> client_secret,
                                    "code" -> code
                                    ) > dispatch.as.oauth.Token
                                 ) 
      for (eth <- message(tokenResponse, "access token")) yield eth.joinRight
   }
}

object Scope extends Enumeration {
	type Scope = Value
	val user, public_repo, repo, gist = Value
}
