package com.drillmap;

import com.drillmap.service.RestServiceDetails;
import com.drillmap.client.ServiceClient;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.x.discovery.ServiceDiscovery;
import com.netflix.curator.x.discovery.ServiceDiscoveryBuilder;
import com.netflix.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ClientApp
{
    private final static Log logger = LogFactory.getLog(ClientApp.class);

    // zookeeper hostname
    @Value("${services.zookeeper.keeperHostName}")
    private String keeperHostName;


    public static void main(final String[] args) throws Exception {

        SpringApplication application = new SpringApplication(ClientApp.class);
        application.setShowBanner(false);
        ApplicationContext context = application.run(args);

        ServiceClient client = (ServiceClient) context.getBean("apiClient");
        try {
            client.printAllPersons();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * To connect to Apache ZooKeeper, we need to create an instance of CuratorFramework class
     *
     * @return
     */
    @Bean( initMethod = "start", destroyMethod = "close" )
    public CuratorFramework curator() {
        logger.info("zooKeeper host: " + this.getKeeperHostName());
        return CuratorFrameworkFactory.newClient(this.getKeeperHostName(), new ExponentialBackoffRetry(1000, 3));
    }

    /**
     * create an instance of ServiceDiscovery class which will allow to publish service information for discovery
     * into Apache ZooKeeper using just created CuratorFramework instance (we also would like to submit
     * RestServiceDetails as additional metadata along with every service registration)
     *
     * @return
     */
    @Bean( initMethod = "start", destroyMethod = "close" )
    public ServiceDiscovery<RestServiceDetails> discovery() {
        JsonInstanceSerializer< RestServiceDetails > serializer =
                new JsonInstanceSerializer< RestServiceDetails >( RestServiceDetails.class );

        return ServiceDiscoveryBuilder.builder(RestServiceDetails.class)
                .client(curator())
                .basePath("services")
                .serializer(serializer)
                .build();
    }

    public String getKeeperHostName() {
        return keeperHostName;
    }

    public void setKeeperHostName(String hostname) {
        this.keeperHostName = hostname;
    }
}
