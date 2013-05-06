package org.eluder.jersey.mustache.examples.guice;

import java.util.concurrent.Callable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.api.view.Viewable;

@Produces("text/html")
@Path("guice")
public class GuiceResource {

    @GET
    public Viewable guice() {
        return new Viewable("/guice.mustache", new Context());
    }
    
    public static class Context {
        
        public Callable<String> first() throws InterruptedException {
            return new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(100);
                    return "This is hard work!";
                }
            };
        }
        
        public Callable<String> second() throws InterruptedException {
            return new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(100);
                    return "This is hard work too!";
                }
            };
        }
    }
}
