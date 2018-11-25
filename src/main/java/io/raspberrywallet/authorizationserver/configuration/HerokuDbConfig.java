package io.raspberrywallet.authorizationserver.configuration;


import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;

@Log
@Configuration
@Profile(Constants.PROFILE_HEROKU)
public class HerokuDbConfig {
    
    @Bean
    public Jedis jedis() {
        try {
            String redistogoUrl = System.getenv("REDISTOGO_URL");
            URI redistogoUri = new URI(redistogoUrl);
            
            return new Jedis(redistogoUri);
            
        } catch (URISyntaxException e) {
            log.severe("Failed to get Redis URL. Message: " + e.getMessage());
            return null;
        }
    }
}
