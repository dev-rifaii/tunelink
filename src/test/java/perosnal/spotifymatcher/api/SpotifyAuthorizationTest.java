package perosnal.spotifymatcher.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Disabled
class SpotifyAuthorizationTest {

    @Autowired
    private SpotifyAuthorization spotifyAuthorization;
    @Test
    void authenticationUrl() {
        System.out.println(spotifyAuthorization.authenticationUrl("http://localhost:8082/"));

    }
}