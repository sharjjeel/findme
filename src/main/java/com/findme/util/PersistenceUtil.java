package com.findme.util;

/**
 * Created by sharjjeel on 7/11/16.
 */
import com.findme.DAO.ItemDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;

import java.net.URI;
import java.util.HashMap;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

public class PersistenceUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static ItemDAO itemDAO;

    public static void buildEntityManagerFactory() {
        if (entityManagerFactory != null) {
            return;
        }
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null) {
            /*
                    <class>com.findme.Entity.ItemEntity</class>
        <properties>
            <property name="javax.persistence.schema-generation.database.action" value="none" />
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true" />
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL82Dialect"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/lostandfound" />
            <property name="javax.persistence.jdbc.driver" value="org.postgresql.Driver" />
            <property name="javax.persistence.jdbc.user" value="test_username" />
            <property name="javax.persistence.jdbc.password" value="test_password" />
        </properties>

             */
            System.out.println("Setting up env with db: " + databaseUrl);
            URI uri = URI.create(databaseUrl);
            HashMap<String, String> properties = new HashMap<>();
            properties.put(SCHEMA_GENERATION_DATABASE_ACTION, "none");
            properties.put(JDBC_DRIVER, "org.postgresql.Driver");
            properties.put(JDBC_URL, "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath());
            properties.put(JDBC_USER, uri.getUserInfo().split(":")[0]);
            properties.put(JDBC_PASSWORD, uri.getUserInfo().split(":")[1]);
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.use_sql_comments", "true");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
            Persistence.createEntityManagerFactory("manager1", properties);
        } else {
            entityManagerFactory = Persistence.createEntityManagerFactory("manager1");
        }
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
