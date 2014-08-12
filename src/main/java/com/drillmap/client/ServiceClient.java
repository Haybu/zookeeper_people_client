package com.drillmap.client;

import com.drillmap.service.Person;
import com.drillmap.service.RestServiceDetails;
import com.netflix.curator.x.discovery.ServiceDiscovery;
import com.netflix.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by hmohamed on 8/11/14.
 */
@Service("apiClient")
public class ServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    @Autowired
    public ServiceDiscovery<RestServiceDetails> discovery;

    public List<Person> getAllPersons() throws Exception {
        logger.info("start getting all persons from zookeeper's services");

        final Collection<ServiceInstance<RestServiceDetails>> services =
                discovery.queryForInstances("peopleService");

        if (services == null) {
            logger.info("no zookeeper services found");
            return null;
        }

        if (services.size() == 0) {
            logger.info("zookeeper services collection is empty");
            return null;
        }

        RestTemplate template = new RestTemplate();

        List<Person> persons = null;

        for (final ServiceInstance<RestServiceDetails> service : services) {
            final String uri = service.buildUriSpec();
            logger.info("rest call to url: " + uri);
            persons = Arrays.asList(template.getForObject(uri, Person[].class));
        }

        return persons;
    }

    public void printAllPersons() throws Exception {
        List<Person> persons = this.getAllPersons();

        if (persons != null) {
            persons.stream().forEach(person -> logger.info("first name: " + person.getFirstName()
                    + ",  last name: " + person.getLastName()));
        } else {
            logger.info("no result is obtained from the restful call");
        }
    }
}
