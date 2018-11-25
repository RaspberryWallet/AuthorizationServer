package io.raspberrywallet.authorizationserver;

import lombok.extern.java.Log;

@Log
@org.springframework.stereotype.Controller
abstract class Controller {
    
    boolean stringsNonEmpty(String... strings) {
        int i = 1;
        for (String string : strings) {
            if (string == null || string.length() == 0) {
                log.severe("[400]Argument on position: " + i + " is null or empty");
                return false;
            }
            
            i++;
        }
        return true;
    }
    
}
