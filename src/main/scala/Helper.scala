package dispatch.github

import dispatch._
import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class JsonObject(private val jsObject: JsObject) {
	def asInt(key: String) = num(jsObject.self(JsString(key))).intValue
	def asString(key: String) = jsObject.self(JsString(key)).self.asInstanceOf[String]
	def asDate(key: String) = parse.date(asString(key).replace("T", " ").replace("Z", ""))
	def asObj(key: String) = JsonObject(obj(jsObject.self(JsString(key))))
	def asList(key: String) = 
		list(jsObject.self(JsString(key))).asInstanceOf[List[JsValue]].map { jsValue => 
			JsonObject(obj(jsValue))
		}
}

object parse {
	def date(dateAsString:String):Date = 
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAsString)
}