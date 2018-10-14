package io.raspberrywallet.authorizationserver;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class APIKeysController {
    
    private static final int DEFAULT_SESSION_LENGTH = 1800;
    
    private CredentialsManager credentialsManager = new CredentialsManager();
    
    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String password = request.getAsString("password");
        
        int sessionLength = DEFAULT_SESSION_LENGTH;
        if (request.containsKey("sessionLength"))
            sessionLength = request.getAsNumber("sessionLength").intValue();

        if (!walletIsRegistered(walletUUID) || !loginWallet(walletUUID, password))
            return ResponseEntity.notFound().build();

        RedisDatabase.delToken(walletUUID);
    
        UUID token = UUID.randomUUID();
        RedisDatabase.setexToken(walletUUID, sessionLength, token);
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
        
        if (RedisDatabase.tokenExists(walletUUID)) {
            String tokenOriginal = RedisDatabase.getToken(walletUUID);
            if (tokenOriginal.equals(token)) {
                RedisDatabase.delToken(walletUUID);
                return ResponseEntity.ok().build();
            }
            else
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        else
            return ResponseEntity.notFound().build();
    }
    
    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody JSONObject request) {
        String walletUUID = request.getAsString("walletUUID");
        String password = request.getAsString("password");
        
        if (walletIsRegistered(walletUUID))
            return new ResponseEntity(HttpStatus.CONFLICT);
        
        byte[] salt = credentialsManager.getSalt();
        byte[] hash = credentialsManager.getHash(password, salt);
        HashAndSalt hashAndSalt = HashAndSalt.builder().salt(salt).hash(hash).build();
        registerNewWallet(walletUUID, hashAndSalt);
        return ResponseEntity.ok().build();
    }
    
    private void registerNewWallet(String walletUUID, HashAndSalt hashAndSalt) {
        RedisDatabase.setHash(walletUUID, hashAndSalt.getHash());
        RedisDatabase.setSalt(walletUUID, hashAndSalt.getSalt());
    }
    
    private boolean walletIsRegistered(String walletUUID) {
        return RedisDatabase.hashExists(walletUUID);
    }
    
}
