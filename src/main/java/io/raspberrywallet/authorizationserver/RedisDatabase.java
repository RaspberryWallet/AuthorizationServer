package io.raspberrywallet.authorizationserver;

import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * It's simply Fascade to Redis database via Jedis.
 */
abstract class RedisDatabase {
    
    private static Jedis jedis = JedisFactory.getJedis();
    
    private RedisDatabase() {}
    
    static String getSecret(String walletUUID) throws ValueNotFoundException {
        if (jedis.exists(JedisKeysFactory.getSecretKey(walletUUID)))
            return jedis.get(JedisKeysFactory.getSecretKey(walletUUID));
        else
            throw new ValueNotFoundException();
    }
    
    static boolean secretExists(String walletUUID) {
        return jedis.exists(JedisKeysFactory.getSecretKey(walletUUID));
    }
    
    static void setSecret(String walletUUID, String secret) {
        jedis.set(JedisKeysFactory.getSecretKey(walletUUID), secret);
    }
    
    static void delSecret(String walletUUID) {
        jedis.del(JedisKeysFactory.getSecretKey(walletUUID));
    }
    
    static String getToken(String walletUUID) {
        return jedis.get(JedisKeysFactory.getTokenKey(walletUUID));
    }
    
    static void delToken(String walletUUID) {
        jedis.del(JedisKeysFactory.getTokenKey(walletUUID));
    }
    
    static boolean tokenExists(String walletUUID) {
        return jedis.exists(JedisKeysFactory.getTokenKey(walletUUID));
    }
    
    static void setexToken(String walletUUID, int sessionLength, UUID token) {
        jedis.setex(JedisKeysFactory.getTokenKey(walletUUID), sessionLength, token.toString());
    }
    
    static byte[] getHash(String walletUUID) {
        return jedis.get(JedisKeysFactory.getHashKey(walletUUID).getBytes());
    }
    
    static byte[] getSalt(String walletUUID) {
        return jedis.get(JedisKeysFactory.getSaltKey(walletUUID).getBytes());
    }
    
    static void setHash(String walletUUID, byte[] hash) {
        jedis.set(JedisKeysFactory.getHashKey(walletUUID).getBytes(), hash);
    }
    
    static void setSalt(String walletUUID, byte[] salt) {
        jedis.set(JedisKeysFactory.getSaltKey(walletUUID).getBytes(), salt);
    }
    
    static boolean hashExists(String walletUUID) {
        return jedis.exists(JedisKeysFactory.getHashKey(walletUUID));
    }
    
    static boolean ping() {
        try {
            String pingResult = jedis.ping();
            return pingResult != null && !pingResult.equals("");
        } catch (Exception e) {
            //catch them all
            return false;
        }
    }
    
}
