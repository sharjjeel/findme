package com.findme;

import com.findme.util.JedisUtil;
import com.findme.util.PersistenceUtil;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.chat package
        final ResourceConfig rc = new ResourceConfig().packages("com.findme");

        // uncomment the following line if you want to enable
        // support for JSON on the service (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml)
        // --
//        rc.register(JacksonJaxbJsonProvider.class);
//        rc.addBinder(org.glassfish.jersey.media.json.JsonJaxbBinder);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI

        URI url = URI.create(BASE_URI);
        if (System.getenv("PORT") != null) {
            url = UriBuilder.fromUri("http://0.0.0.0/").port(Integer.valueOf(System.getenv("PORT"))).build();
        }
        return GrizzlyHttpServerFactory.createHttpServer(url, rc);

    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // create an entity manager factory
        PersistenceUtil.buildEntityManagerFactory();
        PersistenceUtil.buildItemDAO();
        // create redis pool
        JedisUtil.buildJedisPool();
        final HttpServer server = startServer();

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
//        System.in.read();
//        server.shutdown();
//        PersistenceUtil.killEntityManagerFactory();
//        JedisUtil.killJedisPool();
    }
}

