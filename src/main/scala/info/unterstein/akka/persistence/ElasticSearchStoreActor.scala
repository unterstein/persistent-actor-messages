package info.unterstein.akka.persistence

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import scala.util.{Failure, Success}

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {
  import ElasticSearchStoreActor._
  import com.sksamuel.elastic4s.ElasticDsl._
  import scala.concurrent.ExecutionContext.Implicits.global

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case message: InitializedMessage =>
      sender ! (client.client != null)
    case message: StoreMessage =>
      val indexResult = client.scalaClient.execute {
        index into "akka-messages" / message.messageType id UUID.randomUUID.toString.replace("-", "") fields (
          "message" -> message.originalMessage
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

  case class StoreMessage(messageType: String, originalMessage: Any)

  case class NotUnderstandable()

  case class StoreSuccessMessage(id: String)

  case class StoreFailMessage(exception: Throwable)

  def props = Props[ElasticSearchStoreActor]
}