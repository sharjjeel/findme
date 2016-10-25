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
    @Path("/lostItems")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView
    public Response getItemsBasedOnLocation(@QueryParam("longitude") double longitude,
                                            @QueryParam("latitude") double latitude,
                                            @QueryParam("radius") double radius) {
        log.info("getting items from redis");
        List<GeoRadiusResponse> ret;
        try (Jedis jedis = JedisUtil.getJedis()) {
            ret = jedis.georadius("items", longitude, latitude, radius, GeoUnit.KM);
        }
        ItemDAO dao = PersistenceUtil.getItemDAO();
        dao.get(ret);
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
        // add to redis
        try (Jedis jedis = JedisUtil.getJedis()) {
            // default to 1 week
            jedis.geoadd("items", item.getLongitude(), item.getLatitude(), item.getId());
        } catch (Exception e) {
            log.error("unable to save to redis");
        }
        // persist data

        // new DAO vs same DAO?
        ItemDAO dao = PersistenceUtil.getItemDAO();
        ItemEntity itemEntity = dao.create(item);
        return Response.ok(itemEntity.getObject()).build();
    }
}
