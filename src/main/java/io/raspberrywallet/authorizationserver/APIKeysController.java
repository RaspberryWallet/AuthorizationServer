package io.raspberrywallet.authorizationserver;

import lombok.extern.java.Log;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log
@RestController
@RequestMapping("/authorization")
public class APIKeysController extends Controller {
    
    private static final int DEFAULT_SESSION_LENGTH = 1800;
    
    private CredentialsManager credentialsManager = new CredentialsManager();
    
    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String password = request.getAsString("password");
        
        log.info("Login request with JSON:\n" + request.toString());
        
        if (!stringsNonEmpty(walletUUID, password)) {
            return ResponseEntity.badRequest().build();
        }
        
        int sessionLength = DEFAULT_SESSION_LENGTH;
        if (request.containsKey("sessionLength"))
            sessionLength = request.getAsNumber("sessionLength").intValue();

        if (!walletIsRegistered(walletUUID) || !loginWallet(walletUUID, password)) {
            log.warning("[404] Wallet is not registered or password is wrong");
            return ResponseEntity.notFound().build();
        }
        
        RedisDatabase.delToken(walletUUID);
    
        UUID token = UUID.randomUUID();
        RedisDatabase.setexToken(walletUUID, sessionLength, token);
        log.info("[200] Saved new token for wallet");
        return ResponseEntity.ok(token.toString());
    }
    
    private boolean loginWallet(String walletUUID, String password) {
        byte[] hash = RedisDatabase.getHash(walletUUID);
        byte[] salt = RedisDatabase.getSalt(walletUUID);
        
        HashAndSalt hashAndSalt = HashAndSalt.builder().salt(salt).hash(hash).build();
        return credentialsManager.validatePassword(password, hashAndSalt);
    }
    
    @PostMapping(value = "logout")
    public ResponseEntity logout(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String token = request.getAsString("token");
        
        log.info("Logout request with JSON: " + request.toString());
        
        if (!stringsNonEmpty(walletUUID, token)) {
            return ResponseEntity.badRequest().build();
        }
        
        if (RedisDatabase.tokenExists(walletUUID)) {
            String tokenOriginal = RedisDatabase.getToken(walletUUID);
            if (tokenOriginal.equals(token)) {
                RedisDatabase.delToken(walletUUID);
                return ResponseEntity.ok().build();
            }
            else {
                log.warning("[401] Token does not match");
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }
        else
            return ResponseEntity.notFound().build();
    }
    
    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String password = request.getAsString("password");
    
        log.info("Register request with JSON: " + request.toString());
    
        if (!stringsNonEmpty(walletUUID, password))
            return ResponseEntity.badRequest().build();
        
        if (walletIsRegistered(walletUUID)) {
            log.severe("[409] Wallet is already registered");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        
        byte[] salt = credentialsManager.getSalt();
        byte[] hash = credentialsManager.getHash(password, salt);
        HashAndSalt hashAndSalt = HashAndSalt.builder().salt(salt).hash(hash).build();
        registerNewWallet(walletUUID, hashAndSalt);
        
        log.info("[200] Successfully registered");
        return ResponseEntity.ok().build();
    }
    
    @PostMapping(value = "exists")
    public ResponseEntity exists(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
    
        log.info("Exists request with JSON: " + request.toString());
        
        if (!stringsNonEmpty(walletUUID))
            return ResponseEntity.badRequest().build();
    
        if (walletIsRegistered(walletUUID))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    private void registerNewWallet(String walletUUID, HashAndSalt hashAndSalt) {
        RedisDatabase.setHash(walletUUID, hashAndSalt.getHash());
        RedisDatabase.setSalt(walletUUID, hashAndSalt.getSalt());
    }
    
    private boolean walletIsRegistered(String walletUUID) {
        return RedisDatabase.hashExists(walletUUID);
    }
    
    private ResponseEntity badRequest(String msg) {
        log.severe(msg);
        return ResponseEntity.badRequest().build();
    }
    
}
