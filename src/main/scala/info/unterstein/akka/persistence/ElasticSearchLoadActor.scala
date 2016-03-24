package info.unterstein.akka.persistence

import akka.actor.{Props, ActorLogging, Actor}
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchLoadActor extends Actor with ActorLogging {

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
    case message: Any =>
      log.info(s"received $message")
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