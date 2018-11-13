package io.raspberrywallet.authorizationserver;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
abstract class JedisFactory {
    
    private static String host = "redis";
    
    private static int port = 6379;
    
    private static Jedis jedis;
    
    static {
        initialize();
    }
    
    private JedisFactory() {}
    
    static Jedis getJedis() {
        return jedis;
    }
    
    private static void initialize() {
        jedis = new Jedis(host, port);
    }
    
    static void setHost(String host) {
        JedisFactory.host = host;
        initialize();
    }
    
    static void setPort(int port) {
        JedisFactory.port = port;
        initialize();
    }
    
    static void setHostAndPort(String host, int port) {
        JedisFactory.host = host;
        JedisFactory.port = port;
        initialize();
    }
    
}
