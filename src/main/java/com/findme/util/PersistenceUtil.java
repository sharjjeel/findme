package com.findme.util;

/**
 * Created by sharjjeel on 7/11/16.
 */
import com.findme.DAO.ItemDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static ItemDAO itemDAO;

    public static void buildEntityManagerFactory() {
        if (entityManagerFactory != null) {
            return;
        }
        entityManagerFactory = Persistence.createEntityManagerFactory("manager1");
    }

    public static void buildItemDAO() {
        if (itemDAO != null) {
            return;
        }
        itemDAO = new ItemDAO();
    }

    public static ItemDAO getItemDAO() {
        return itemDAO;
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void killEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
