package org.eluder.jersey.mustache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.api.view.Viewable;

@Produces("text/html")
@Path("mustache")
public class MustacheResource {

    @GET
    public Viewable mustache() {
        return new Viewable("/template.mustache", new Context());
    }
    
    public static class Context {
        public String value = "bar";
    }
}
