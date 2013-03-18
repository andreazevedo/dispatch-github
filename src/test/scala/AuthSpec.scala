package dispatch.github.specs

import org.specs2.mutable._
import org.specs2.execute.Pending
import dispatch.github._

class AuthSpec extends Specification {
	val clientId:String = "ad42e1f93976bcaa61f2"
	val clientSecret:String = "4d7eb4d41efbcb9c25e3884526991d2bed397591"
	val redirectUri:String = "dispatchgithubtests.com"

	"When getting the github authorization url" should {

		"return a Promise that contains to the correct url when calling Auth.webapp_authorize_uri" in {
			val authRes = Auth.webapp_authorize_uri(clientId, redirectUri)
         val redirectStr = authRes()
         
         redirectStr must equalTo("https://github.com/login?return_to=%2Flogin%2Foauth%2Fauthorize%3Fclient_id%3D" + clientId + "%26redirect_uri%3D" + redirectUri)
		}
	}
}
