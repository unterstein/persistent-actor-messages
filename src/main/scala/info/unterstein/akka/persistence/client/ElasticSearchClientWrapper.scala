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
  // TODO maybe DI is a better solution..
  def getByConfiguration: ElasticSearchClientWrapper = if("remote" == ConfigFactory.load().getString("elastic.mode")) new RemoteElasticSearchClientWrapper() else new EmbeddedElasticSearchClientWrapper()
}
