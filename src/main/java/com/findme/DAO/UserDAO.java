package com.findme.DAO;

import com.findme.Entity.UserEntity;
import com.findme.model.User;
import com.findme.util.PersistenceUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;
import java.util.List;

/**
 * Created by saziz on 7/28/16.
 */
public class UserDAO {
    public UserEntity create(User user) {
        if (user == null)
            throw new WebApplicationException("User is null", Response.Status.BAD_REQUEST);
        if (user.getName() == null) {
            throw new WebApplicationException("User name is null", Response.Status.BAD_REQUEST);
        }
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();

        UserEntity userEntity = makeUserEntity(user);

        em.persist(userEntity);
        em.flush();
        em.getTransaction().commit();
        return userEntity;
    }

    private UserEntity makeUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setPassword(user.getPassword());

        return userEntity;
    }

    public UserEntity delete(User user) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();
        findUser(em, user);

        UserEntity userEntity = makeUserEntity(user);

        em.remove(userEntity);
        em.flush();
        em.getTransaction().commit();
        return userEntity;
    }

    private UserEntity findUser(EntityManager em, User user) {
        try {
            return em.createQuery("SELECT o FROM users o WHERE o.name = :user_name AND o.password = :password",
                    UserEntity.class).setParameter("name", user.getName())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new WebApplicationException("Unable to find user", Response.Status.FORBIDDEN);
        }
    }

    public UserEntity checkUserExistence(User user) {
        EntityManager em = PersistenceUtil.getEntityManager();
        return findUser(em, user);
    }
}
