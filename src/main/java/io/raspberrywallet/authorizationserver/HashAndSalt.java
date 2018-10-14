package io.raspberrywallet.authorizationserver;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class HashAndSalt {
    
    private byte[] hash;
    private byte[] salt;
    
}
