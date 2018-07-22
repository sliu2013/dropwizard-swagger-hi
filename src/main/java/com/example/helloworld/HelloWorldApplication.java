package com.example.helloworld;

import com.example.helloworld.api.CustomerDAO;
import com.example.helloworld.resources.CustomerResource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.example.helloworld.resources.HelloWorldResource;
import com.example.helloworld.health.TemplateHealthCheck;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {

        environment.jersey().register(new ApiListingResource());


        final HelloWorldResource helloWorldResource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(helloWorldResource);


        final CustomerResource customerApiResource = new CustomerResource(new CustomerDAO());
        environment.jersey().register(customerApiResource);


        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);


        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        BeanConfig config = new BeanConfig();
        config.setTitle("Swagger sample app");
        config.setVersion("1.0.0");
        config.setResourcePackage("com.example.helloworld.resources");
        config.setScan(true);

//        initSwagger(configuration, environment);
    }

//    private void initSwagger(HelloWorldConfiguration configuration, Environment environment) {
//        // Swagger Resource
//        environment.jersey().register(new ApiListingResourceJSON());
//
//        // Swagger providers
//        environment.jersey().register(new ApiDeclarationProvider());
//        environment.jersey().register(new ResourceListingProvider());
//
//        // Swagger Scanner, which finds all the resources for @Api Annotations
//        ScannerFactory.setScanner(new DefaultJaxrsScanner());
//
//        // Add the reader, which scans the resources and extracts the resource information
//        ClassReaders.setReader(new DefaultJaxrsApiReader());
//
//        // required CORS support
//        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
//        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//        filter.setInitParameter("allowedOrigins", "*"); // allowed origins comma separated
//        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
//        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
//        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
//        filter.setInitParameter("allowCredentials", "true");
//
//        // Set the swagger config options
//        SwaggerConfig config = ConfigFactory.config();
//        config.setApiVersion("1.0.1");
//        config.setBasePath(configuration.getSwaggerBasePath());
//    }

}
