package info.unterstein.akka.persistence.api

import com.google.gson._
import java.lang.reflect.Type

import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
case class PersistentActorMessage(messageType: String, scheduleDate: Long, originalMessage: Map[String, String])

object PersistentActorMessage {

  private val mapToken = new TypeToken[java.util.Map[String, String]](){}.getType

  private val log = LoggerFactory.getLogger(PersistentActorMessage.getClass)

  private val gson = new GsonBuilder()
    .registerTypeAdapter(classOf[Map[String, String]], new MapSerializer())
    .create()

  private class MapSerializer extends JsonSerializer[Map[String, String]] with JsonDeserializer[Map[String, String]] {

    override def serialize(src: Map[String, String], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      gson.toJsonTree(src.asJava)
    }

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map[String, String] = {
      val result: java.util.Map[String, String] = gson.fromJson(json, mapToken)
      result.asScala.toMap
    }
  }

  def jsonToMap(json: String): Map[String, String] = {
    try {
      val result: java.util.Map[String, String] = gson.fromJson(json, mapToken)
      result.asScala.toMap
    } catch {
      case o_O: Exception =>
        log.error("Deserialization failed for " + json, o_O)
        throw new RuntimeException("Deserialization failed!", o_O)
    }
  }

  def mapToJson(map: Map[String, String]): String = gson.toJson(map)
}
