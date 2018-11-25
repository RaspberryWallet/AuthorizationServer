package io.raspberrywallet.authorizationserver;

import org.springframework.context.annotation.Configuration;

@Configuration
class JedisKeysFactory {
    
    private final String TOKEN_HEADER = "token:";
    private final String WALLET_HEADER = "wallet:";
    private final String SALT_HEADER = "salt:";
    private final String SECRET_HEADER = "secret:";
    
    public String getTokenKey(String walletUUID) {
        return TOKEN_HEADER + walletUUID;
    }
    
    public String getHashKey(String walletUUID) {
        return WALLET_HEADER + walletUUID;
    }
    
    public String getSaltKey(String walletUUID) {
        return SALT_HEADER + walletUUID;
    }
    
    public String getSecretKey(String walletUUID) {
        return SECRET_HEADER + walletUUID;
    }
    
}
