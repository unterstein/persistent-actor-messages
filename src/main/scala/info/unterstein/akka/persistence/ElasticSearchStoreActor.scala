package info.unterstein.akka.persistence

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.sksamuel.elastic4s.source.Indexable
import info.unterstein.akka.persistence.api.PersistentActorElasticSearchMessage
import info.unterstein.akka.persistence.client.ElasticSearchClientWrapper
import org.elasticsearch.action.index.IndexRequest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class ElasticSearchStoreActor extends Actor with ActorLogging {
  import ElasticSearchStoreActor._
  import com.sksamuel.elastic4s.ElasticDsl._
  import scala.concurrent.ExecutionContext.Implicits.global

  val client = ElasticSearchClientWrapper.getByConfiguration

  def receive = {
  	case message: InitializedMessage =>
      sender ! (client.client != null)
    case StoreMessage(originalMessage: Any) =>
      val indexResult = client.scalaClient.execute {
        index into "akka2" / "messages" id UUID.randomUUID.toString.replace("-", "") fields (
          "message" -> originalMessage
          )
      }
//      indexResult.onComplete {
//        result =>
//          if(result.isSuccess) {
//            sender ! StoreSuccessMessage(result.get.id)
//          } else {
//            sender ! StoreFailMessage
//          }
//      }
      val result = Await.result(indexResult, Duration.Inf)
      sender ! StoreSuccessMessage(result.id)

    case other: Any =>
      self.tell(StoreMessage(other), sender)
  }

}

object ElasticSearchStoreActor {

  case class InitializedMessage()

  case class StoreMessage(originalMessage: Any)

  case class StoreSuccessMessage(id: String)

  case class StoreFailMessage(exception: Throwable)

  def props = Props[ElasticSearchStoreActor]
}