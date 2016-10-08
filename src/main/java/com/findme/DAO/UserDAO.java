package com.findme.DAO;

import com.findme.Entity.ItemEntity;
import com.findme.Entity.UserEntity;
import com.findme.model.Item;
import com.findme.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Created by saziz on 7/28/16.
 */
public class UserDAO {
    public List<UserEntity> getAll() {
        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM users o", UserEntity.class).getResultList();
    }
    public UserEntity create(User user) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();

        if (user == null)
            throw new WebApplicationException("User doesn't exist", Response.Status.BAD_REQUEST);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        em.persist(userEntity);
        em.flush();
        em.getTransaction().commit();
        return userEntity;
    }
}
