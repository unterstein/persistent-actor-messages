package info.unterstein.akka.persistence.client

import com.sksamuel.elastic4s.ElasticClient
import org.elasticsearch.client.Client

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
trait ElasticSearchClientWrapper {

  def client: Client

  def scalaClient: ElasticClient = ElasticClient.fromClient(client)

  def close(): Unit
}
