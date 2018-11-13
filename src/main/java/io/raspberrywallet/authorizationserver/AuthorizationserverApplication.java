package io.raspberrywallet.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;


@SpringBootApplication
public class AuthorizationserverApplication {
    
    public static void main(String[] args) {
        setGlobalLogLevel(Level.ALL);
        SpringApplication.run(AuthorizationserverApplication.class, args);
    }
    
    private static void setGlobalLogLevel(Level level) {
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
}
