package io.raspberrywallet.authorizationserver;

import redis.clients.jedis.Jedis;

abstract class JedisFactory {
    
    private static Jedis jedis = new Jedis("localhost", 6379);
    
    private JedisFactory() {}
    
    static Jedis getJedis() {
        return jedis;
    }
    
}
