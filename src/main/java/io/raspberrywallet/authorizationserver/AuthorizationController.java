package io.raspberrywallet.authorizationserver;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorization/secret")
public class  AuthorizationController extends Controller {
    
    @PostMapping(value = "get")
    ResponseEntity<String> getSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        
        if (!stringsNonEmpty(walletUUID, token))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        try {
            return new ResponseEntity<>(RedisDatabase.getSecret(walletUUID), HttpStatus.OK);
        } catch (ValueNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    private boolean isAuthorized(String walletUUID, String token) {
        String originalToken = RedisDatabase.getToken(walletUUID);
        return originalToken.equals(token);
    }
    
    @PostMapping(value = "set")
    ResponseEntity setSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        String secret = request.getAsString("secret");
    
        if (!stringsNonEmpty(walletUUID, token, secret))
            return ResponseEntity.badRequest().build();
        
        if (!isAuthorized(walletUUID, token))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    
        if (RedisDatabase.secretExists(walletUUID))
            return new ResponseEntity(HttpStatus.CONFLICT);
    
        RedisDatabase.setSecret(walletUUID, secret);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "overwrite")
    ResponseEntity overwritteSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        String secret = request.getAsString("secret");
    
        if (!stringsNonEmpty(walletUUID, token, secret))
            return ResponseEntity.badRequest().build();
        
        if (secret == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        
        if (!isAuthorized(walletUUID, token))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    
        RedisDatabase.setSecret(walletUUID, secret);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "remove")
    ResponseEntity removeSecret(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
    
        if (!stringsNonEmpty(walletUUID, token))
            return ResponseEntity.badRequest().build();
    
        if (!isAuthorized(walletUUID, token))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        if (!RedisDatabase.secretExists(walletUUID))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    
        RedisDatabase.delSecret(walletUUID);
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
