package skymeet;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import skymeet.resource.FlightResources;

// The Java class will be hosted at the URI path "/helloworld"
public class Publisher {

    public static final String resourceFlightsNear = "flightsNear";
    private static final int PORT = 9095;
    private static final String URL = "http://localhost:" + PORT + "/";

    public static void main(String[] args) {
        final URI baseUri = UriBuilder.fromUri(URL).build();
        ResourceConfig resourceConfig = new ResourceConfig(FlightResources.class);
        JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig, true);
        System.out.println("Service hosted at " + baseUri +  resourceFlightsNear);

    }
}
