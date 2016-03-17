package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}
import com.google.gson.Gson
import com.sksamuel.elastic4s.source.Indexable
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {
  import ElasticSearchStoreActor._
  import com.sksamuel.elastic4s.ElasticDsl._

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case message: InitializedMessage =>
      sender ! (client.client != null)
    case StoreMessage(originalMessage: Any) =>
      client.scalaClient.execute {
        index into "akka-messages" source
      }
  }

}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage(originalMessage: Any)

  def props = Props[ElasticSearchStoreActor]
}