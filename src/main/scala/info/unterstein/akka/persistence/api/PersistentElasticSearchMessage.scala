package info.unterstein.akka.persistence.api

import com.google.gson.Gson

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
case class PersistentElasticSearchMessage(id: Long, message: Any)

object PersistentElasticSearchMessage {
  val gson = new Gson()

  def toJson(entity: PersistentElasticSearchMessage): String = gson.toJson(entity)

  def fromJson(json: String) = gson.fromJson(json, classOf[PersistentElasticSearchMessage])
}
