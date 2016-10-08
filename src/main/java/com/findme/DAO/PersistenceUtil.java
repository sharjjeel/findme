package com.findme.DAO;

/**
 * Created by sharjjeel on 7/11/16.
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static ItemDAO itemDAO;
    private static UserDAO userDAO;

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

    public static void buildUserDAO() {
        if (userDAO != null) {
            return;
        }
        userDAO = new UserDAO();
    }

    public static UserDAO getUserDAO() {
        return userDAO;
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
