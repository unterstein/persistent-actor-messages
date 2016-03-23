package info.unterstein.akka.persistence

import akka.actor.{ActorLogging, Actor}
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

