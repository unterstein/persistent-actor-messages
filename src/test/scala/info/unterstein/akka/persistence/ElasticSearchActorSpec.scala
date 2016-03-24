package info.unterstein.akka.persistence

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import info.unterstein.akka.persistence.ElasticSearchLoadActor.{LoadSuccessMessage, LoadMessage}
import info.unterstein.akka.persistence.ElasticSearchStoreActor.{StoreSuccessMessage, StoreMessage, InitializedMessage}
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
class ElasticSearchActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  def this() = this(ActorSystem("ElasticSearchActorSpec"))

  val cleanIndex = ElasticSearchClientWrapper.getByConfiguration.scalaClient.execute(deleteIndex(ElasticSearchClientWrapper.messageIndex))

  Await.ready(cleanIndex, 2 seconds)

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }
 
  "An ElasticSearchStoreActor actor" must {
    "must have a client" in {
      val storeActor = system.actorOf(ElasticSearchStoreActor.props)
      storeActor ! ElasticSearchStoreActor.InitializedMessage()
      expectMsg(true)
    }
  }

  "An ElasticSearchStoreActor actor" must {
    "must store a StoreMessage in ElasticSearch" in {
      val storeActor = system.actorOf(ElasticSearchStoreActor.props)
      storeActor ! StoreMessage(messageType = "test", originalMessage = "test")
      expectMsgAllClassOf(classOf[StoreSuccessMessage])
    }
  }

  "An ElasticSearchLoadActor actor" must {
    "must have a client" in {
      val loadActor = system.actorOf(ElasticSearchLoadActor.props)
      loadActor ! ElasticSearchLoadActor.InitializedMessage()
      expectMsg(true)
    }
  }

  "An ElasticSearchLoadActor actor" must {
    "must load a previsouly stored message" in {
      val storeActor = system.actorOf(ElasticSearchStoreActor.props)
      storeActor ! StoreMessage(messageType = "test", originalMessage = "test")
      val storeAnswer = receiveOne(2 seconds).asInstanceOf[StoreSuccessMessage]

      val loadActor = system.actorOf(ElasticSearchLoadActor.props)
      loadActor ! LoadMessage(messageType = "test", id = storeAnswer.id)
      val loadAnswer = receiveOne(10 seconds).asInstanceOf[LoadSuccessMessage]
      println(loadAnswer)
    }
  }

}
