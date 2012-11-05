package dispatch.github.specs

import org.specs._
import dispatch.github._

class AuthSpec extends Specification {
	val clientId:String = "ad42e1f93976bcaa61f2"
	val clientSecret:String = "4d7eb4d41efbcb9c25e3884526991d2bed397591"
	val	redirectUri:String = "dispatchgithubtests.com"

	"When getting the github authorization url" should {

		"return an object of the dispatch.Request when calling Auth.authorize_uri" in {
			Auth.authorize_uri(clientId, redirectUri) must haveClass[dispatch.Request]
		}

		"return a request that points to the correct url when calling Auth.authorize_uri" in {
			Auth.authorize_uri(clientId, redirectUri).to_uri.toString must equalTo("https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri)
		}
	}
/*
	"The Request returned by authorize_uri, when called" should {
		"do something" in {
			Auth.authorize_uri(clientId, clientSecret)
		}
	}
*/
}
