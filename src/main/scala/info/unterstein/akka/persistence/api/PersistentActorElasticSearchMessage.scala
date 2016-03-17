package info.unterstein.akka.persistence.api

import com.google.gson.Gson

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
case class PersistentActorElasticSearchMessage(id: Long, message: Any)

object PersistentActorElasticSearchMessage {
  val gson = new Gson()

  def toJson(entity: PersistentActorElasticSearchMessage): String = gson.toJson(entity)

  def fromJson(json: String) = gson.fromJson(json, classOf[PersistentActorElasticSearchMessage])
}
