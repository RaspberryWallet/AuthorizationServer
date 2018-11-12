package io.raspberrywallet.authorizationserver;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/internal")
public class InternalController extends Controller {
    
    @GetMapping(value = "redis/isOnline")
    public ResponseEntity isRedisOnline() {
        log.info("Is Redis online request.");
        if (RedisDatabase.ping()) {
            log.info("[200] Redis is online.");
            return ResponseEntity.ok("true");
        }
        else {
            log.info("[200] Redis is offline.");
            return ResponseEntity.ok("false");
        }
    }
}
