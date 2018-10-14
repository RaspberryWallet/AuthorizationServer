package io.raspberrywallet.authorizationserver;

abstract class JedisKeysFactory {
    
    private static final String TOKEN_HEADER = "token:";
    private static final String WALLET_HEADER = "wallet:";
    private static final String SALT_HEADER = "salt:";
    private final static String SECRET_HEADER = "secret:";
    
    private JedisKeysFactory() {}
    
    public static String getTokenKey(String walletUUID) {
        return TOKEN_HEADER + walletUUID;
    }
    
    public static String getHashKey(String walletUUID) {
        return WALLET_HEADER + walletUUID;
    }
    
    public static String getSaltKey(String walletUUID) {
        return SALT_HEADER + walletUUID;
    }
    
    public static String getSecretKey(String walletUUID) {
        return SECRET_HEADER + walletUUID;
    }
    
}
