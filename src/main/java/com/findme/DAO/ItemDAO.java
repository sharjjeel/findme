package com.findme.DAO;

import com.findme.Entity.ItemEntity;
import com.findme.Entity.UserEntity;
import com.findme.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by sharjjeel on 7/10/16.
 */
public class ItemDAO {

    public List<ItemEntity> getAll() {
        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM items o", ItemEntity.class).getResultList();
    }

    public ItemEntity create(Item item) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();
        UserEntity user = em.createQuery("SELECT o FROM users o where o.id = :user_id", UserEntity.class)
                .setParameter("user_id", item.getUser_id()).getSingleResult();

        if (user == null)
            throw new WebApplicationException("User doesn't exist", Response.Status.NOT_FOUND);
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setClaim_status(item.getClaim_status());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setId(item.getId());
        itemEntity.setLocation(item.getLocation());
        itemEntity.setLost(item.isLost());
        itemEntity.setTimestamp(item.getTimestamp());
        itemEntity.setUser(user);
        itemEntity.setName(item.getName());

        em.persist(itemEntity);
        em.flush();
        em.getTransaction().commit();
        return itemEntity;
    }
}
