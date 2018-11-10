package io.raspberrywallet.authorizationserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationserverApplicationTests {
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    public void ContextLoads() {
    }
    
    @Test
    public void RedisIsOnline() throws Exception {
        mvc.perform(get("/internal/redis/isOnline")).andExpect(status().isOk());
    }

}
