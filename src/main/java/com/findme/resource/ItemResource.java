package com.findme.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.findme.DAO.ItemDAO;
import com.findme.DAO.PersistenceUtil;
import com.findme.Entity.ItemEntity;
import com.findme.model.Item;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Path("item")
public class ItemResource {

    Logger log = LoggerFactory.logger(ItemResource.class);
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/healthcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public String healthCheck() {
        return "I'm alive\n";
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getThreads(@DefaultValue("20") @QueryParam("count") int count) {
//        log.info("Getting last "+ count + " messages");
//        // TODO: get last {count} updated threads
//        ThreadDAO threadDAO = new ThreadDAO();
//        return Response.ok(threadDAO.getLastUpdated(count)).build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response getItems() {
        log.info("getting items");
        ItemDAO dao = PersistenceUtil.getItemDAO();
        log.info(dao.getAll());
        return Response.ok(new GenericEntity<List<ItemEntity>>(dao.getAll()){}).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response addItem(Item item) {
        log.info("adding item");
        if (item.getUser_id() == null) {
            throw new WebApplicationException("User must be specified", Response.Status.BAD_REQUEST);
        }
        if (item.getId() == null) {
            item.setId(UUID.randomUUID().toString());
        }
        ItemDAO dao = PersistenceUtil.getItemDAO();
        ItemEntity itemEntity = dao.create(item);
        return Response.ok(itemEntity.getObject()).build();
    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getThreads() {
//        log.info("getting threads");
//        ThreadDAO threadDAO = new ThreadDAO();
//        return Response.ok(threadDAO.getAll()).build();
//    }
//
//    @GET
//    @Path("/{thread_name}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMessages(@PathParam("thread_name") String thread_name,
//                                @DefaultValue("20") @QueryParam("count") int count) {
//        MessageDAO messageDAO = new MessageDAO();
//        return Response.ok(messageDAO.getAllMessagesForThread(thread_name)).build();
//    }
}
