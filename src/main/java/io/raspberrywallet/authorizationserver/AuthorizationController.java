package io.raspberrywallet.authorizationserver;

import lombok.extern.java.Log;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("/authorization/secret")
public class  AuthorizationController extends Controller {
    
    private final RedisDatabase redisDatabase;
    
    @Autowired
    public AuthorizationController(RedisDatabase redisDatabase) {
        this.redisDatabase = redisDatabase;
    }
    
    @PostMapping(value = "get")
    public ResponseEntity<String> getSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        
        log.info("Get request with JSON: " + request.toString());
        
        if (!stringsNonEmpty(walletUUID, token))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token)) {
            log.warning("[401] Wrong session token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        try {
            String secret = redisDatabase.getSecret(walletUUID);
            log.info("[200] Returning secret with hash: " + secret.hashCode());
            return new ResponseEntity<>(redisDatabase.getSecret(walletUUID), HttpStatus.OK);
        } catch (ValueNotFoundException e) {
            log.warning("[404] Secret not found for this wallet");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    private boolean isAuthorized(String walletUUID, String token) {
        String originalToken = redisDatabase.getToken(walletUUID);
        return originalToken.equals(token);
    }
    
    @PostMapping(value = "set")
    public ResponseEntity setSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        String secret = request.getAsString("secret");
    
        log.info("Set request with JSON: " + request.toString());
    
        if (!stringsNonEmpty(walletUUID, token, secret))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token)) {
            log.severe("[401] Wrong session token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    
        if (redisDatabase.secretExists(walletUUID)) {
            log.severe("[409] Secret is already set for this wallet");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    
        redisDatabase.setSecret(walletUUID, secret);
        log.info("[200] Secret set with hash code: " + secret.hashCode());
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "overwrite")
    public ResponseEntity overwritteSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        String secret = request.getAsString("secret");
    
        log.info("Overwrite request with JSON: " + request.toString());
    
        if (!stringsNonEmpty(walletUUID, token, secret))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token)) {
            log.severe("[401] Wrong session token");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    
        redisDatabase.setSecret(walletUUID, secret);
        log.info("[200] Secret overwritten with hash code: " + secret.hashCode());
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "exists")
    public ResponseEntity isSecretSet(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        
        log.info("Secret exists request with JSON: " + request.toString());
        
        if (!stringsNonEmpty(walletUUID, token))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token)) {
            log.severe("[401] Wrong session token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        if (redisDatabase.secretExists(walletUUID)) {
            log.info("[200] Secret exists");
            return ResponseEntity.ok().build();
        } else {
            log.info("[404] Secret not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping(value = "remove")
    public ResponseEntity removeSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
    
        log.info("Remove request with JSON: " + request.toString());
    
        if (!stringsNonEmpty(walletUUID, token))
            return ResponseEntity.badRequest().build();
    
        if (!isAuthorized(walletUUID, token)) {
            log.severe("[401] Wrong session token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        if (!redisDatabase.secretExists(walletUUID)) {
            log.severe("[404] Secret not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    
        redisDatabase.delSecret(walletUUID);
        log.info("[200] Secret removed");
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
