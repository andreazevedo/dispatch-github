package dispatch.github

import dispatch._
import json._
import JsHttp._
import java.util.{Calendar, Date, TimeZone}
import java.text.SimpleDateFormat

case class JsonObject(private val jsObject: JsObject) {
	def apply(key: String):JsonValue = JsonValue(jsObject, key)
	def contains(key: String) :Boolean = jsObject.self.contains(JsString(key)) && jsObject.self(JsString(key)).self != null
}

case class JsonValue(private val jsObject:JsObject, private val key:String) {
	def asInt = num(jsObject.self(JsString(key))).intValue
	def asString = jsObject.self(JsString(key)).self.asInstanceOf[String]
	def asDate = parse.date(this.asString.replace("Z", ""))
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
	def date(dateAsString:String):Date = {
		val dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		dateParser.setTimeZone(TimeZone.getTimeZone("GMT"))
		dateParser.parse(dateAsString)
	}
}

object DateTimeHelper {
	def createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, miliseconds: Int, timeZone: String): Date = {
		val gmtCal = getCalendar(timeZone)
		gmtCal.set(year, (month - 1), day, hour, minute, second)
		gmtCal.set(Calendar.MILLISECOND, 0)
		gmtCal.getTime
	}
	def createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, timeZone: String): Date =
		createDate(year, month, day, hour, minute, second, 0, timeZone)
	def createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date =
		createDate(year, month, day, hour, minute, second, 0, null)

	private def getCalendar(timeZone: String) = 
		if(timeZone != null && timeZone != ""){
			Calendar.getInstance(TimeZone.getTimeZone(timeZone))
		} else {
			Calendar.getInstance()
		}		
}