package com.findme.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.findme.DAO.ItemDAO;
import com.findme.DAO.PersistenceUtil;
import com.findme.DAO.UserDAO;
import com.findme.Entity.ItemEntity;
import com.findme.Entity.UserEntity;
import com.findme.model.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("users")
public class UserResource {

    Logger log = LoggerFactory.logger(UserResource.class);

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
    public Response getUsers() {
        log.info("getting users");
        UserDAO dao = PersistenceUtil.getUserDAO();
        return Response.ok(dao.getAll()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JsonView
    public Response addUser(User user) {
        log.info("adding user");
        UserDAO dao = PersistenceUtil.getUserDAO();
        UserEntity userEntity = dao.create(user);
        return Response.ok(userEntity).build();
    }
}