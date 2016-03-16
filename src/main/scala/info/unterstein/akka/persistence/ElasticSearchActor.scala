package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchActor extends Actor with ActorLogging {
  import ElasticSearchActor._

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case s: InitializedMessage =>
      sender ! (client.client != null)
  }
}

object ElasticSearchActor {

  case class InitializedMessage()

  case class StoreMessage()

  def props = Props[ElasticSearchActor]
}