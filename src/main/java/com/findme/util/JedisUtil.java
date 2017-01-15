package com.findme.util;

/**
 * Created by sharjjeel on 7/11/16.
 */
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.ws.rs.WebApplicationException;
import java.net.URI;
import java.net.URISyntaxException;

public class JedisUtil {

    static JedisPool pool;

    public static void buildJedisPool() {
        if (pool != null) {
            return;
        }

        if (System.getenv("REDIS_URL") != null) {
            try {
                URI redisURI = new URI(System.getenv("REDIS_URL"));
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(10);
                poolConfig.setMaxIdle(5);
                poolConfig.setMinIdle(1);
                poolConfig.setTestOnBorrow(true);
                poolConfig.setTestOnReturn(true);
                poolConfig.setTestWhileIdle(true);
                pool = new JedisPool(poolConfig, redisURI);
            } catch (URISyntaxException e) {
                throw new WebApplicationException("Unable to initiate redis", e);
            }
        } else {
            pool = new JedisPool(new JedisPoolConfig(), "localhost");
        }
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void killJedisPool() {
        if (pool != null) {
            pool.destroy();
        }
    }
}
