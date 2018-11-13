package io.raspberrywallet.authorizationserver;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
public class RedisTests {
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    public void RedisIsOnline() throws Exception {
        mvc.perform(get("/internal/redis/isOnline")).andExpect(status().isOk());
    }
    
}
