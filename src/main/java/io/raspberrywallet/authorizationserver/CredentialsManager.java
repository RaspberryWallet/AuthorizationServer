package io.raspberrywallet.authorizationserver;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

@Service
public class CredentialsManager {
    
    private final Random random = new SecureRandom();
    private static final int HASH_ITERATIONS = 100;
    private static final int HASH_LENGTH_IN_BYTES = 128;
    
    
    byte[] getSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    
    byte[] getHash(final String password, final byte[] salt) {
        byte[] hash;
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATIONS, HASH_LENGTH_IN_BYTES * 8);
            hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        
        return hash;
    }
    
    boolean validatePassword(final String password, final HashAndSalt hashAndSalt) {
        byte[] generatedHash = getHash(password, hashAndSalt.getSalt());
        return Arrays.equals(generatedHash, hashAndSalt.getHash());
    }
    
}
