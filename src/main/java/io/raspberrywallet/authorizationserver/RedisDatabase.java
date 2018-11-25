package io.raspberrywallet.authorizationserver;

import io.raspberrywallet.authorizationserver.configuration.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * It's simply Fascade to Redis database via Jedis.
 */
@Service
class RedisDatabase {
    
    private Jedis jedis;
    private final JedisKeysFactory jedisKeysFactory;
    
    @Autowired
    public RedisDatabase(Jedis jedis, JedisKeysFactory jedisKeysFactory) {
        
        // Jedis bean is not autowired
        // find why :(
        
        this.jedis = jedis;
        this.jedisKeysFactory = jedisKeysFactory;
    }
    
    String getSecret(String walletUUID) throws ValueNotFoundException {
        if (jedis.exists(jedisKeysFactory.getSecretKey(walletUUID)))
            return jedis.get(jedisKeysFactory.getSecretKey(walletUUID));
        else
            throw new ValueNotFoundException();
    }
    
    boolean secretExists(String walletUUID) {
        return jedis.exists(jedisKeysFactory.getSecretKey(walletUUID));
    }
    
    void setSecret(String walletUUID, String secret) {
        jedis.set(jedisKeysFactory.getSecretKey(walletUUID), secret);
    }
    
    void delSecret(String walletUUID) {
        jedis.del(jedisKeysFactory.getSecretKey(walletUUID));
    }
    
    String getToken(String walletUUID) {
        return jedis.get(jedisKeysFactory.getTokenKey(walletUUID));
    }
    
    void delToken(String walletUUID) {
        jedis.del(jedisKeysFactory.getTokenKey(walletUUID));
    }
    
    boolean tokenExists(String walletUUID) {
        return jedis.exists(jedisKeysFactory.getTokenKey(walletUUID));
    }
    
    void setexToken(String walletUUID, int sessionLength, UUID token) {
        jedis.setex(jedisKeysFactory.getTokenKey(walletUUID), sessionLength, token.toString());
    }
    
    byte[] getHash(String walletUUID) {
        return jedis.get(jedisKeysFactory.getHashKey(walletUUID).getBytes());
    }
    
    byte[] getSalt(String walletUUID) {
        return jedis.get(jedisKeysFactory.getSaltKey(walletUUID).getBytes());
    }
    
    void setHash(String walletUUID, byte[] hash) {
        jedis.set(jedisKeysFactory.getHashKey(walletUUID).getBytes(), hash);
    }
    
    void setSalt(String walletUUID, byte[] salt) {
        jedis.set(jedisKeysFactory.getSaltKey(walletUUID).getBytes(), salt);
    }
    
    boolean hashExists(String walletUUID) {
        return jedis.exists(jedisKeysFactory.getHashKey(walletUUID));
    }
    
    boolean ping() {
        try {
            String pingResult = jedis.ping();
            return pingResult != null && !pingResult.equals("");
        } catch (Exception e) {
            //catch them all
            return false;
        }
    }
    
}
