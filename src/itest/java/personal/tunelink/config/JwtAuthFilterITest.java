package personal.tunelink.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import perosnal.tunelink.jwt.JwtManager;
import perosnal.tunelink.ping.PingApi;
import personal.tunelink.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtAuthFilterITest extends IntegrationTestBase {

    @Autowired
    PingApi pingApi;
    @Autowired
    JwtManager jwtManager;

    @Test
    void pingApiCall_ReturnsUnauthorized_WhenAuthHeaderIsNull() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void pingApiCall_ReturnsUnauthorized_WhenAuthHeaderIsInvalid() throws Exception {
        mockMvc.perform(get("/ping")
                        .header("Authorization", "Bearer someRandomToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void pingApiCall_ReturnsPong_WhenAuthHeaderIsValid() throws Exception {
        var jwt = jwtManager.generateJwt("101");
        var userId = jwtManager.extractSub(jwt);
        mockMvc.perform(get("/ping")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("pong:" + userId));
    }
}
