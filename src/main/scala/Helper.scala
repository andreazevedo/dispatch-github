package dispatch.github

import dispatch._
import json._
import JsHttp._

import java.util.Date
import java.text.SimpleDateFormat

case class JsonObject(private val jsObject: JsObject) {
	def apply(key: String):JsonValue = JsonValue(jsObject, key)
}

case class JsonValue(private val jsObject:JsObject, private val key:String) {
	def asInt = num(jsObject.self(JsString(key))).intValue
	def asString = jsObject.self(JsString(key)).self.asInstanceOf[String]
	def asDate = parse.date(this.asString.replace("T", " ").replace("Z", ""))
	def asObj = JsonObject(obj(jsObject.self(JsString(key))))
	def asList = 
		list(jsObject.self(JsString(key))).asInstanceOf[List[JsValue]].map { jsValue => 
			JsonObject(obj(jsValue))
		}
}

object parse {
	def jsonObj(json: JsValue) = JsonObject(obj(json))
	def jsonList(json: JsValue) = list(json).asInstanceOf[List[JsValue]].map { jsValue =>
			JsonObject(obj(jsValue))
		}
	def date(dateAsString:String):Date = 
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAsString)
}