package com.findme.DAO;

import com.findme.Entity.ItemEntity;
import com.findme.Entity.UserEntity;
import com.findme.model.Item;
import com.findme.resource.ItemResource;
import com.findme.util.JedisUtil;
import com.findme.util.PersistenceUtil;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

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

    Logger log = LoggerFactory.logger(ItemDAO.class);

    public List<ItemEntity> getAll() {
        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM items o", ItemEntity.class).getResultList();
    }

    public List<ItemEntity> get(double longitude, double latitude, double radius) {

        // get list of items ids from redis
        List<GeoRadiusResponse> ret;
        try (Jedis jedis = JedisUtil.getJedis()) {
            ret = jedis.georadius("items", longitude, latitude, radius, GeoUnit.KM);
        }

        List<String> ids = new ArrayList<>();
        for (GeoRadiusResponse grr: ret) {
            ids.add(grr.getMemberByString());
        }

        EntityManager em = PersistenceUtil.getEntityManager();
        return em.createQuery("SELECT o FROM items o WHERE o.id in (:ids) AND o.lost = TRUE",
            ItemEntity.class).setParameter("ids", ids).getResultList();
    }

    public ItemEntity create(Item item) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();

        UserEntity user;
        try {
            user = em.createQuery("SELECT o FROM users o where o.id = :user_id", UserEntity.class)
                    .setParameter("user_id", item.getUser_id()).getSingleResult();
        } catch (Exception e) {
            log.info(e.toString());
            throw new WebApplicationException("User doesn't exist: " + e.toString(), Response.Status.NOT_FOUND);
        }

        ItemEntity itemEntity = createItemEntity(item, user);
        em.persist(itemEntity);
        em.flush();

        em.getTransaction().commit();

        log.info("Finished postgres persistence");

        try (Jedis jedis = JedisUtil.getJedis()) {
            // default to 1 week?
            jedis.geoadd("items", item.getLongitude(), item.getLatitude(), item.getId());
        } catch (Exception e) {
            log.error("unable to save to redis");
        }

        return itemEntity;
    }

    public ItemEntity update(Item item) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();

        UserEntity user;
        try {
            user = em.createQuery("SELECT o FROM users o where o.id = :user_id", UserEntity.class)
                    .setParameter("user_id", item.getUser_id()).getSingleResult();
        } catch (NoResultException e) {
            throw new WebApplicationException("User doesn't exist", Response.Status.NOT_FOUND);
        }
        // TODO: remove if it's not lost from redis
        // TODO: update redis longitude, latitude of item if that is updated
        ItemEntity itemEntity = createItemEntity(item, user);
        em.merge(itemEntity);
        em.flush();

        em.getTransaction().commit();
        return itemEntity;
    }
    public ItemEntity delete(Item item) {
        EntityManager em = PersistenceUtil.getEntityManager();
        em.getTransaction().begin();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(item.getId());
        em.remove(itemEntity);
        em.flush();

        em.getTransaction().commit();

        try (Jedis jedis = JedisUtil.getJedis()) {
            jedis.zrem("items", item.getId());
        } catch (Exception e) {
            log.error("unable to delete from redis");
        }

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
