package info.unterstein.akka.persistence.api

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson._
import java.lang.reflect.Type
import scala.collection.JavaConverters._

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
case class PersistentActorMessage(messageType: String, message: Any)

object PersistentActorMessage {

  val dateFormat = new SimpleDateFormat()

  val gson = new GsonBuilder()
    .registerTypeAdapter(classOf[List[Any]], new ListSerializer())
    .registerTypeAdapter(classOf[Map[Any, Any]], new MapSerializer())
    .registerTypeAdapter(classOf[Date], new DateSerializer())
    .create()


  class ListSerializer extends JsonSerializer[List[Any]] {

    override def serialize(src: List[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      gson.toJsonTree(src.asJava)
    }
  }

  class MapSerializer extends JsonSerializer[Map[Any, Any]] {

    override def serialize(src: Map[Any, Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      gson.toJsonTree(src.asJava)
    }
  }

  class DateSerializer extends JsonSerializer[Date] {

    override def serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      gson.toJsonTree(dateFormat.format(src))
    }
  }

  def toJson(entity: PersistentActorMessage): String = toJsonAny(entity)

  def toJsonAny(entity: Any): String = gson.toJson(entity)

  def fromJson(json: String): PersistentActorMessage = fromJsonAny(json, classOf[PersistentActorMessage])

  def fromJsonAny[T](json: String, classOf: Class[T]): T = gson.fromJson(json, classOf)
}
