package info.unterstein.akka.persistence.client

import org.elasticsearch.client.Client

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
trait ElasticSearchClientWrapper {

  def client: Client

  def close(): Unit
}
