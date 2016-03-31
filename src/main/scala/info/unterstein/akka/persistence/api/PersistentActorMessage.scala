package info.unterstein.akka.persistence.api

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
case class PersistentActorMessage(messageType: String, scheduleDate: Option[Long] = None, originalMessage: AnyRef)
