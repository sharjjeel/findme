package com.findme.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.findme.DAO.ItemDAO;
import com.findme.util.JedisUtil;
import com.findme.util.PersistenceUtil;
import com.findme.Entity.ItemEntity;
import com.findme.model.Item;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.util.Base64;
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
        return "I'm alive";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response getItems() {
        log.info("getting items");
        // use Inject?
        ItemDAO dao = PersistenceUtil.getItemDAO();
        log.info(dao.getAll());
        return Response.ok(new GenericEntity<List<ItemEntity>>(dao.getAll()){}).build();
    }

    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response getItemsBasedOnLocation(@QueryParam("longitude") double longitude,
                                            @QueryParam("latitude") double latitude,
                                            @QueryParam("radius") double radius) {
        log.info("getting items from redis: "+ longitude + " " + latitude);
        ItemDAO dao = PersistenceUtil.getItemDAO();
        return Response.ok(new GenericEntity<List<ItemEntity>>(dao.get(longitude, latitude, radius)){}).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response addItem(Item item) {

        item.setTimestamp(System.currentTimeMillis() + "");
        if (item.getId() == null) {
            try {
                String id = Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(item.toString().getBytes("UTF-8")));
                item.setId(id);
            } catch (Exception e) {
                log.error(e);
                throw new WebApplicationException("Unable to create id", Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        log.info("adding item: " + item.toString());

        // new DAO vs same DAO?
        ItemDAO dao = PersistenceUtil.getItemDAO();
        ItemEntity itemEntity = dao.create(item);
        return Response.ok(itemEntity.getObject()).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response update(Item item) {
        if (item.getId() == null) {
            throw new WebApplicationException("Id must be specified", Response.Status.BAD_REQUEST);
        }
        ItemDAO dao = PersistenceUtil.getItemDAO();
        dao.update(item);
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response delete(Item item) {
        if (item.getId() == null) {
            throw new WebApplicationException("Id must be specified", Response.Status.BAD_REQUEST);
        }
        ItemDAO dao = PersistenceUtil.getItemDAO();
        dao.delete(item);
        return Response.ok().build();
    }
}
