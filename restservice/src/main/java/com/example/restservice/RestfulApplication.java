package com.example.restservice;

import com.example.restservice.resources.RestfulResource;
import io.dropwizard.Application;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RestfulApplication extends Application<RestfulConfiguration> {

    public static void main(String[] args) throws Exception {
        new RestfulApplication().run(args);
    }

    @Override
    public void run(RestfulConfiguration restfulConfiguration, Environment environment) throws Exception {
        final RestfulResource restfulResource = new RestfulResource();
        environment.jersey().register(restfulResource);
    }



    // DiscoveryBundle & initialize() are making the zookeeper instance as the service-registry
    private final DiscoveryBundle<RestfulConfiguration> discoveryBundle = new DiscoveryBundle<RestfulConfiguration>() {
        @Override
        public DiscoveryFactory getDiscoveryFactory(RestfulConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }

    };
    @Override
    public void initialize(Bootstrap<RestfulConfiguration> bootstrap) {
        bootstrap.addBundle(discoveryBundle);
    }
}