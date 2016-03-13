package com.example

import akka.actor.{Actor, ActorLogging, Props}

class PingActor extends Actor with ActorLogging {
  import PingActor._
  
  var counter = 0

  def receive = {
  	case Initialize => 
	    log.info("In PingActor - starting ping-pong")
  }
}

object PingActor {
  val props = Props[PingActor]
  case object Initialize
  case class PingMessage(text: String)
}