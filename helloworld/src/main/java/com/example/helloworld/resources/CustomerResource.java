package com.example.helloworld.resources;

import com.example.helloworld.api.Customer;
import com.example.helloworld.api.CustomerDAO;
import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(value = "customers", description = "RESTful API to interact with customer resources.")
@Path("customers")
public class CustomerResource {

//    @Injector
    private CustomerDAO dao;

    public CustomerResource(CustomerDAO dao){
        this.dao = dao;
    }


    @ApiOperation(value = "Get all customers", notes = "Get all customers matching the given search string.", responseContainer = "List", response = Customer.class)
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers(
            @ApiParam(value = "The search string is used to find customer by their name. Not case sensetive.", required = false, defaultValue = "") @QueryParam("search") String searchString,
            @ApiParam(value = "Limits the size of the result set", required = false, defaultValue = "50") @QueryParam("limit") int limit) {
        List<Customer> customers = dao.getCustomers(searchString, limit);
        return customers;
    }

    @ApiOperation(value = "Create a new customer", notes = "Creates a new customer with the given name. The URL of the new customer is returned in the location header.")
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmployee(
            @ApiParam(value = "customer's name", required = true) String customerName)
            throws URISyntaxException {
        Customer customer = dao.createCustomer(customerName);

        URI uri = createNewLocationURI(customer.getId());
        Response response = Response.created(uri).build();
        return response;
    }

    private URI createNewLocationURI(int id) {
        return null;
    }
}
