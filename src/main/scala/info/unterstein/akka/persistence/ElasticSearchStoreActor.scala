package info.unterstein.akka.persistence

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import info.unterstein.akka.persistence.api.PersistentActorMessage
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import scala.util.{Failure, Success}
import ElasticSearchStoreActor._
import com.sksamuel.elastic4s.ElasticDsl._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case message: InitializedMessage =>
      sender ! (client.client != null)
    case message: StoreMessage =>
      val indexResult = client.scalaClient.execute {
        index into ElasticSearchClientWrapper.messageIndex / message.messageType id UUID.randomUUID.toString.replace("-", "") fields (
          ElasticSearchClientWrapper.messageFieldName -> PersistentActorMessage(message.messageType, message.scheduleDate, message.originalMessage).toJson
        )
      }
      val originalSender = sender
      indexResult onComplete {
        case Success(result) => originalSender ! StoreSuccessMessage(result.getId)
        case Failure(exception) => originalSender ! StoreFailMessage(exception)
      }
    case other => sender ! NotUnderstandable()
  }
}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage(messageType: String, scheduleDate: Long = System.currentTimeMillis(), originalMessage: Map[String, String])

  case class NotUnderstandable()

  case class StoreSuccessMessage(id: String)

  case class StoreFailMessage(exception: Throwable)

  def props = Props[ElasticSearchStoreActor]
}