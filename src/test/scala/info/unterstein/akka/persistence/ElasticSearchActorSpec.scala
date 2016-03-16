package info.unterstein.akka.persistence

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import info.unterstein.akka.persistence.ElasticSearchActor.InitializedMessage
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
class ElasticSearchActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  def this() = this(ActorSystem("ElasticSearchActorSpec"))
 
  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }
 
  "An ElasticSearchActor actor" must {
    "must have a client" in {
      val elasticSearchActor = system.actorOf(ElasticSearchActor.props)
      elasticSearchActor ! InitializedMessage()
      expectMsg(20 seconds, true)
    }
  }

}
