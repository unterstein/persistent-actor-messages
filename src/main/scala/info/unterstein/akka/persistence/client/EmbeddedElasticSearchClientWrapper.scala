package info.unterstein.akka.persistence.client

import com.typesafe.config.ConfigFactory
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.NodeBuilder

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class EmbeddedElasticSearchClientWrapper extends ElasticSearchClientWrapper {


  val config = ConfigFactory.load()
  val settings = Settings.settingsBuilder()
    .put("cluster.name", config.getString("elastic.cluster.name"))
    .put("path.home", config.getString("elastic.path.home"))
    .put("http.port", config.getString("elastic.http.port"))

  val node = NodeBuilder.nodeBuilder().local(true).settings(settings).node()
  val embeddedClient = node.client()

  override def client: Client = embeddedClient

  override def close(): Unit = {
    node.close()
    client.close()
  }
}
