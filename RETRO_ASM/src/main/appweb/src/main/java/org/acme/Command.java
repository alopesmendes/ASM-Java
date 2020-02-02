package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/webapp")
public class Command {
	private String path;
	private String commandline;
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        var text = "Application du web service du RetroCompiler\n"
        		+ "Toujours en cours d'implémentation";
        return text;
    }
    
    
    @POST
    public void getCommandline(Command command) {
    	this.path = command.path;
    	this.commandline = command.commandline;
    	System.out.println(path);
    	System.out.println(commandline);
    	
    }
    

}