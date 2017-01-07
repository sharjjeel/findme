package com.findme.util;

/**
 * Created by sharjjeel on 7/11/16.
 */
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    static JedisPool pool;

    public static void buildJedisPool() {
        if (pool != null) {
            return;
        }
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
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
