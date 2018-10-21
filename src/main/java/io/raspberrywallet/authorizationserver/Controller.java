package io.raspberrywallet.authorizationserver;

abstract class Controller {
    
    boolean stringsNonEmpty(String... strings) {
        for (String string : strings) {
            if (string == null || string.length() == 0)
                return false;
        }
        return true;
    }
    
}
