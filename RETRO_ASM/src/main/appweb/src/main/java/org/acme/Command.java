package org.acme;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/retro")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class Command {
    String path;
    String options;

    @POST
    public String test(){
        return null;
    }





    @GET
    public String hello() {
        var text = "BOUH";
        return text;
    }
}
