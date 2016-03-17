package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}
import com.sksamuel.elastic4s.source.Indexable
import info.unterstein.akka.persistence.api.PersistentActorElasticSearchMessage
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {
  import ElasticSearchStoreActor._
  import com.sksamuel.elastic4s.ElasticDsl._
  import scala.concurrent.ExecutionContext.Implicits.global

  val client = ElasticSearchClientWrapper.getByConfiguration

  implicit object CharacterIndexable extends Indexable[PersistentActorElasticSearchMessage] {
    override def json(persistentMessage: PersistentActorElasticSearchMessage): String = PersistentActorElasticSearchMessage.toJson(persistentMessage)
  }

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
        case exception: Throwable => sender ! StoreFailMessage(exception)
        case o_O: Any => throw new RuntimeException(o_O)
      }
  }

}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage(originalMessage: Any)

  case class StoreSuccessMessage(id: String)

  case class StoreFailMessage(exception: Throwable)

  def props = Props[ElasticSearchStoreActor]
}