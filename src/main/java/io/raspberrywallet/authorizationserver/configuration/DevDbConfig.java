package io.raspberrywallet.authorizationserver.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.clients.jedis.Jedis;

@Configuration
@Profile(Constants.PROFILE_DEV)
public class DevDbConfig {
	
    @Bean
    public Jedis jedis() {
        return new Jedis("redis", 6379);
    }
}
