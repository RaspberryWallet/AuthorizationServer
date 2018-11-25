package io.raspberrywallet.authorizationserver;

import io.raspberrywallet.authorizationserver.configuration.Constants;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;

@Log
@SpringBootApplication
public class AuthorizationserverApplication {
    
    private final Environment env;
    
    @Autowired
    public AuthorizationserverApplication(Environment env) {
        this.env = env;
    }
    
    /**
     * Spring profiles can be configured with program arguments --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() {
        if (env.getActiveProfiles().length == 0) {
            log.warning("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : " + Arrays.toString(env.getActiveProfiles()));
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationserverApplication.class, args);

//        SpringApplication app = new SpringApplication(AuthorizationserverApplication.class);
//        setGlobalLogLevel(Level.ALL);
//
//        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
//        if (!source.containsProperty("spring.profiles.active") &&
//                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
//
//            app.setAdditionalProfiles(Constants.PROFILE_DEV);
//        }
    }
    
    private static void setGlobalLogLevel(Level level) {
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
    
}
