package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}
import info.unterstein.akka.persistence.api.PersistentActorElasticSearchMessage
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
      val indexResult = client.scalaClient.execute {
        index into "akka-messages" source new PersistentActorElasticSearchMessage(null, originalMessage)
      }
      indexResult.onComplete {
        result =>
          if(result.isSuccess) {
            sender ! StoreSuccessMessage(result.get.id)
          } else {
            sender ! StoreFailMessage
          }
      }
      indexResult.onFailure {
        case exception: Exception => sender ! StoreFailMessage(exception)
      }
  }

}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage(originalMessage: Any)

  case class StoreSuccessMessage(id: String)

  case class StoreFailMessage(exception: Exception)

  def props = Props[ElasticSearchStoreActor]
}