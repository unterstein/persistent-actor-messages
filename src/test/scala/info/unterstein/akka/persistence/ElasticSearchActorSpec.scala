package info.unterstein.akka.persistence

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import info.unterstein.akka.persistence.ElasticSearchStoreActor.{StoreSuccessMessage, StoreMessage, InitializedMessage}
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
      val elasticSearchActor = system.actorOf(ElasticSearchStoreActor.props)
      elasticSearchActor ! InitializedMessage()
      expectMsg(true)
    }
  }

  "An ElasticSearchActor actor" must {
    "must store a StoreMessage in ElasticSearch" in {
      val elasticSearchActor = system.actorOf(ElasticSearchStoreActor.props)
      elasticSearchActor ! StoreMessage(messageType = "test", originalMessage = "test")
      expectMsgAllClassOf(classOf[StoreSuccessMessage])
    }
  }

}
