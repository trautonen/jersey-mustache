package org.eluder.jersey.mustache.examples.simple;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sun.jersey.api.view.Viewable;

@Produces("text/html")
@Path("simple")
public class SimpleResource {

    @GET
    public Viewable simple(@QueryParam("value") final Integer value) {
        return new Viewable("/simple.mustache", new Context(value));
    }
    
    public static class Context {
        public Integer value;

        public Context(final Integer value) {
            this.value = value;
        }
    }
    
}
