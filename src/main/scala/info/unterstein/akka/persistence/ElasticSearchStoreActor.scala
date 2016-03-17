package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {
  import ElasticSearchStoreActor._

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case s: InitializedMessage =>
      sender ! (client.client != null)
  }
}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage()

  def props = Props[ElasticSearchStoreActor]
}