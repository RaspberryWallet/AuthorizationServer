package io.raspberrywallet.authorizationserver;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@Log
@RestController
@RequestMapping("/internal")
public class InternalController extends Controller {
    
    private RedisDatabase redisDatabase;
    
    @Autowired
    public InternalController(RedisDatabase redisDatabase) {
        this.redisDatabase = redisDatabase;
    }
    
    @GetMapping(value = "redis/isOnline")
    public ResponseEntity isRedisOnline() {
        log.info("Is Redis online request.");
        if (redisDatabase.ping()) {
            log.info("[200] Redis is online.");
            return ResponseEntity.ok("true");
        }
        else {
            log.info("[200] Redis is offline.");
            return ResponseEntity.ok("false");
        }
    }
}
