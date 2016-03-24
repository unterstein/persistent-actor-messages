package info.unterstein.akka.persistence

import akka.actor.{Props, ActorLogging, Actor}
import com.sksamuel.elastic4s.ElasticDsl.index
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import ElasticSearchLoadActor._

import scala.util.{Failure, Success}

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchLoadActor extends Actor with ActorLogging {

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
    case message: Any =>
    case message: InitializedMessage =>
      sender ! (client.client != null)
    case message: LoadMessage =>
      //
    case other => sender ! NotUnderstandable()
  }
}

object ElasticSearchLoadActor {

  case class InitializedMessage()

  case class LoadMessage(messageType: String, id: String)

  case class NotUnderstandable()

  case class LoadSuccessMessage(id: String)

  case class LoadFailMessage(exception: Throwable)

  def props = Props[ElasticSearchLoadActor]
}