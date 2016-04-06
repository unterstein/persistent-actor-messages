package info.unterstein.akka.persistence

import akka.actor.{Props, ActorLogging, Actor}
import com.sksamuel.elastic4s.ElasticDsl._
import info.unterstein.akka.persistence.api.PersistentActorMessage
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import ElasticSearchLoadActor._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._

import scala.util.{Failure, Success}

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchLoadActor extends Actor with ActorLogging {

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
    case message: InitializedMessage =>
      sender ! (client.client != null)
    case message: LoadScheduledMessage =>
      val searchResult = client.scalaClient.execute {
        search in ElasticSearchClientWrapper.messageIndex / message.messageType query {
          rangeQuery(ElasticSearchClientWrapper.scheduleFieldName) from 0 to message.scheduleDate
        }
      }
      val originalSender = sender
      searchResult onComplete {
        case Success(result) => {
          val resultList = result.getHits.map {
            hit =>
              LoadSuccessMessage(hit.getId, PersistentActorMessage.jsonToMap(hit.getSource.get(ElasticSearchClientWrapper.messageFieldName).toString))
          }
          originalSender ! LoadScheduledSuccessMessage(resultList.toList)
        }
        case Failure(o_O) => {
          log.error(o_O, "Unable to load scheduled messages")
          originalSender ! LoadFailMessage(o_O)
        }
      }
    case message: LoadMessage =>
      val getResult = client.scalaClient.execute {
       get id message.id from ElasticSearchClientWrapper.messageIndex / message.messageType
      }
      val originalSender = sender
      getResult onComplete {
        case Success(result) => {
          originalSender ! LoadSuccessMessage(result.getId, PersistentActorMessage.jsonToMap(result.source.get(ElasticSearchClientWrapper.messageFieldName).toString))
        }
        case Failure(o_O) => {
          log.error(o_O, s"Unable to load dedicated message ${message.id}")
          originalSender ! LoadFailMessage(o_O)
        }
      }
    case other => sender ! NotUnderstandable()
  }
}

object ElasticSearchLoadActor {

  case class InitializedMessage()

  case class LoadMessage(messageType: String, id: String)

  case class LoadScheduledMessage(messageType: String, scheduleDate: Long = System.currentTimeMillis())

  case class NotUnderstandable()

  case class LoadSuccessMessage(id: String, message: Map[String, String])

  case class LoadScheduledSuccessMessage(result: List[LoadSuccessMessage])

  case class LoadFailMessage(exception: Throwable)

  def props = Props[ElasticSearchLoadActor]
}