package com.example.helloworld;

import com.example.helloworld.api.CustomerDAO;
import com.example.helloworld.resources.CustomerResource;
import io.dropwizard.Application;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;
import io.dropwizard.discovery.client.DiscoveryClientManager;
import io.dropwizard.discovery.core.InstanceMetadata;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.example.helloworld.resources.HelloWorldResource;
import com.example.helloworld.health.TemplateHealthCheck;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.apache.curator.x.discovery.ServiceCacheBuilder;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProviderBuilder;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static DiscoveryClient restServiceClient;

    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }



    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) throws Exception {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final CustomerResource swaggerApiResource = new CustomerResource(new CustomerDAO());
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(swaggerApiResource);



        // register a new service to consume other services registered in Zookeeper instance
        restServiceClient = discoveryBundle.newDiscoveryClient("restful-hi");
        environment.lifecycle().manage(new DiscoveryClientManager(restServiceClient));
    }


    // DiscoveryBundle & initialize() are making the zookeeper instance as the service-registry
    public static final DiscoveryBundle<HelloWorldConfiguration> discoveryBundle = new DiscoveryBundle<HelloWorldConfiguration>() {
        @Override
        public DiscoveryFactory getDiscoveryFactory(HelloWorldConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }

    };

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<HelloWorldConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(HelloWorldConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
        bootstrap.addBundle(discoveryBundle);
    }
}