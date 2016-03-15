package info.unterstein.akka.persistence.client

import java.net.InetAddress

import com.typesafe.config.ConfigFactory
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

/**
  * @author Johannes Unterstein (unterstein@me.com)
  */
class RemoteElasticSearchClientWrapper extends ElasticSearchClientWrapper {

  // configure client
  val config = ConfigFactory.load
  val settings = Settings.settingsBuilder().put("cluster.name", config.getString("elastic.cluster.name"))

  // introduce client
  val remoteClient = TransportClient.builder().settings(settings).build()
  remoteClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(config.getString("elastic.host")), config.getInt("elastic.port")))

  override def client: Client = remoteClient

  override def close(): Unit = client.close()
}
