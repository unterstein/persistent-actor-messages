package info.unterstein.akka.persistence.client

import com.sksamuel.elastic4s.ElasticClient
import com.typesafe.config.ConfigFactory
import org.elasticsearch.client.Client

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
trait ElasticSearchClientWrapper {

  def client: Client

  def scalaClient: ElasticClient = ElasticClient.fromClient(client)

  def close(): Unit

}

object ElasticSearchClientWrapper {

  val messageIndex = "akka-messages"

  // TODO maybe DI is a better solution..
  private val client = if("remote" == ConfigFactory.load().getString("elastic.mode")) new RemoteElasticSearchClientWrapper() else new EmbeddedElasticSearchClientWrapper()

  def getByConfiguration: ElasticSearchClientWrapper = client
}
