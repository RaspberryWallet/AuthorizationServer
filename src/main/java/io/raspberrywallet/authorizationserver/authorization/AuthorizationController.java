package io.raspberrywallet.authorizationserver.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.security.Principal;

@RestController
@RequestMapping("/authorization/secret")
public class  AuthorizationController {
    
    @Value("${redis.host}")
    private String redisHost;
    
    @Value("${redis.port}")
    private int port;
    
    private Jedis jedis = new Jedis("localhost", 6379);
    
    @GetMapping(value = "/get")
    ResponseEntity<ByteArrayResource> getSecret() {
        String username = getUsername();
        
        if (jedis.exists(username)) {
            byte[] secret = jedis.get(username.getBytes());
            ByteArrayResource byteArrayResource = new ByteArrayResource(secret);
            return new ResponseEntity<>(byteArrayResource, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(value = "set")
    ResponseEntity setSecret(@RequestParam String secret) {
        String username = getUsername();
        
        if (jedis.exists(username)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        else {
            jedis.set(username.getBytes(), secret.getBytes());
            return new ResponseEntity(HttpStatus.OK);
        }
        
    }
    
    @PostMapping(value = "overwritte")
    ResponseEntity overwritteSecret(@RequestParam String secret) {
        String username = getUsername();
        
        jedis.set(username.getBytes(), secret.getBytes());
        return new ResponseEntity(HttpStatus.OK);
    }
    
    private String getUsername() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return principal.getName();
    }
}
