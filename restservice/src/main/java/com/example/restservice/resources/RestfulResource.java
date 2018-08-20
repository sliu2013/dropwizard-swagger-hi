package com.example.restservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Api(value = "restful-hi", description = "RESTful sample API")
@Path("restful-hi")
public class RestfulResource {

    @ApiOperation(value = "Get current timestamp", notes = "...", response = String.class)
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCurrentTimeStamp(){
        return DateTime.now().toString();
    }

}
