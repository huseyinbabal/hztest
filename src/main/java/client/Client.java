package client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.ConfigBuilder;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Properties;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().setConnectionAttemptLimit(1000);
        config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true).setProperties(props));
        config.setGroupConfig(new GroupConfig("<cluster_name>", "<group_password>"));
        String token = "<token>";
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), token);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), "https://coordinator.hazelcast.cloud");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap map = client.getMap("map");
        for (int i = 0; i < 10000000; i++) {
            char[] data = new char[10000000];
            String str = new String(data);
            map.put(i, str);
            System.out.println(map.size());
        }
        while (true) {
            System.out.println("map size:" + map.size());
            Thread.sleep(3000);
        }
    }

}
