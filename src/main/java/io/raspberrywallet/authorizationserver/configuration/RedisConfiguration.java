package io.raspberrywallet.authorizationserver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfiguration {
    
    private Jedis jedis;
    
    @Autowired
    public RedisConfiguration(Jedis jedis) {
        this.jedis = jedis;
    }
    
}
