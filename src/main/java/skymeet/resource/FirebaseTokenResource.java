package skymeet.resource;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import skymeet.data.FileManager;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("firebase/tokens")
public class FirebaseTokenResource {


    public static String demoToken;

    @POST
    @Path("{token}")
    public Response createToken(@PathParam("token") String token) {
        demoToken = token;
        System.out.println();
        System.out.println("Received TOKEN: " + token);

        FileManager.getInstance().writeTokenToFile(demoToken);
        return Response.ok().build();
    }

}
