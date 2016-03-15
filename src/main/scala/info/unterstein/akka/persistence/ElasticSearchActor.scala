package info.unterstein.akka.persistence

import akka.actor.{Actor, ActorLogging, Props}

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchActor extends Actor with ActorLogging {
  import ElasticSearchActor._

  var counter = 0

  def receive = {
  	case obj: Any=>
	    log.info("In PingActor - starting ping-pong")
      sender ! "pong"
  }
}

object ElasticSearchActor {
  def props = Props[ElasticSearchActor]
}