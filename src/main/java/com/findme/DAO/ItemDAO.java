package com.findme.DAO;

import com.findme.Entity.ItemEntity;
import com.findme.Entity.UserEntity;
import com.findme.model.Item;
import com.findme.util.PersistenceUtil;
import redis.clients.jedis.GeoRadiusResponse;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharjjeel on 7/10/16.
 */
public class ItemDAO {

    public List<ItemEntity> getAll() {
        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM items o", ItemEntity.class).getResultList();
    }

    public List<ItemEntity> get(List<GeoRadiusResponse> list) {
        ArrayList<String> ids = new ArrayList<>();
        for (GeoRadiusResponse grr: list) {
            ids.add(grr.getMemberByString());
        }
        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM items o WHERE o.id in (:ids)",
            ItemEntity.class).setParameter("ids", ids).getResultList();
    }

    public ItemEntity create(Item item) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();
        UserEntity user;
        try {
            user = em.createQuery("SELECT o FROM users o where o.id = :user_id", UserEntity.class)
                    .setParameter("user_id", item.getUser_id()).getSingleResult();
        } catch (NoResultException e) {
            throw new WebApplicationException("User doesn't exist", Response.Status.NOT_FOUND);
        }

        ItemEntity itemEntity = createItemEntity(item, user);

        em.persist(itemEntity);
        em.flush();
        em.getTransaction().commit();
        return itemEntity;
    }

    private ItemEntity createItemEntity(Item item, UserEntity user) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setDescription(item.getDescription());
        itemEntity.setId(item.getId());
        itemEntity.setLongitude(item.getLongitude());
        itemEntity.setLatitude(item.getLatitude());
        itemEntity.setLost(item.isLost());
        itemEntity.setTimestamp(item.getTimestamp());
        itemEntity.setUser(user);
        itemEntity.setName(item.getName());
        return itemEntity;
    }
}
