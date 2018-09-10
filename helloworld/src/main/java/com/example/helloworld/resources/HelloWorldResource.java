package com.example.helloworld.resources;

import com.example.helloworld.HelloWorldApplication;
import com.example.helloworld.api.Saying;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.discovery.client.DiscoveryClient;
import org.apache.curator.x.discovery.ServiceInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) throws Exception {
        final String value = String.format(template, name.orElse(defaultName));

        Collection<ServiceInstance> serviceInstances = HelloWorldApplication.restServiceClient.getInstances();
        String address = "", serviceName = "";
        Integer port = 80;
        for (ServiceInstance serviceInstance : serviceInstances){
            address = serviceInstance.getAddress();
            port = serviceInstance.getPort();
            serviceName = serviceInstance.getName();
            System.out.println("service ip: " + address);
            System.out.println("service port: " + port);
            System.out.println("service name: " + serviceName);
        }

        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target(String.format("http://%s:%d/%s", address, port, serviceName));

        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);

        Response response = request.get();

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            System.out.println("Success! " + response.getStatus());
//            System.out.println(response.getEntity());
            System.out.println(response.readEntity(String.class));
        } else {
            System.out.println("ERROR! " + response.getStatus());
//            System.out.println(response.getEntity());
        }

        return new Saying(counter.incrementAndGet(), value);
    }

    @GET
    @Path("/firstname")
    @Timed
    public String sayFirstName(@QueryParam("name") Optional<String> name) throws Exception {
        DiscoveryClient client = HelloWorldApplication.discoveryBundle.newDiscoveryClient("restful-hi");
//        DiscoveryClientManager clientManager = new DiscoveryClientManager(client);
//        clientManager.start();
        System.out.println("hi" + client.getInstances().size());
        System.out.println("hey" + client.getInstances("restful-hi").toString());
        System.out.println("hello" + client.getInstances("/dropwizard/service/restful-hi").toString());
        System.out.println("clap" + client.getServices().size());

        return "firstname, hello";
    }
}
