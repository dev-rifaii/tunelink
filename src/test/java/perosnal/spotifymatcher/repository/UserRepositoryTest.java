package perosnal.spotifymatcher.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import perosnal.spotifymatcher.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {


    @Autowired
    UserRepository userRepository;

    @Test
    void shouldPersistUser() {
        User user = User.builder()
                .country("EE")
                .id("test-user-id")
                .tracks(new ArrayList<>())
                .email("email")
                .build();

        userRepository.save(user);
        assertNotNull(userRepository.getById("test-user-id"));
    }

    @Test
    void shouldFindUsersByCountry() {
        userRepository.save(User.builder().id("test1").country("EE").build());
        userRepository.save(User.builder().id("test2").country("EE").build());
        userRepository.save(User.builder().id("test3").country("US").build());

        List<User> estonianUsers = userRepository.findByCountry("EE");

        assertFalse(estonianUsers.stream().anyMatch("test3"::equals));
    }

    @Test
    void shouldGetUserById() {
        User user = userRepository.getById("0Rgn0_first_id");
        assertNotNull(user);
    }

    @Test
    void shouldGetTracksByUser() {
        List<String> tracks = userRepository.findTracksById("0Rgn0_first_id");
        assertTrue(tracks.size() > 0);
    }


    @Test
    void shouldGetUserByAccessToken() {
        User user = userRepository.findByTokenAccessToken("0Rgn0_first_access_token");
        assertNotNull(user);
    }


    @Test
    void matching() {
        List<User> matches = userRepository.getMatches("0Rgn0_first_id", 1);
        assertTrue(matches.size() > 0);
    }

}

