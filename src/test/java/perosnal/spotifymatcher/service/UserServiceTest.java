package perosnal.spotifymatcher.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;
import perosnal.spotifymatcher.util.AuthorizedActionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SpotifyApiService spotifyApiService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
     void before() {
        lenient().when(spotifyApiService.getIdByToken("")).thenReturn("");
        lenient().when(userRepository.getById("")).thenReturn(new User());
    }

    @Test
    void given_short_bio_should_return_failure() {
        AuthorizedActionResult result = userService.setBio("", "123");
        assertEquals(result, AuthorizedActionResult.FAILURE);
    }

    @Test
    void given_null_bio_should_return_failure() {
        AuthorizedActionResult result = userService.setBio("", null);
        assertEquals(result, AuthorizedActionResult.FAILURE);
    }

    //Biography should be 20 characters or above
    @Test
    void given_valid_bio_should_return_success() {
        AuthorizedActionResult result = userService.setBio("", "123456789Test123456789");
        assertEquals(result, AuthorizedActionResult.SUCCESS);
    }



    @Test
    void given_already_found_match_should_filter(){
        User user = validUser();
        user.getMatches().add(validMatch().getId());
        List<User> matches = userService.filterMatches(user, new ArrayList<>(Arrays.asList(validMatch())));
        assertTrue(matches.isEmpty());
    }
    @Test
    void given_match_with_empty_bio_should_filter() {
        List<User> matches = userService.filterMatches(validUser(), new ArrayList<>(Arrays.asList(userWithEmptyBio())));
        assertTrue(matches.isEmpty());
    }

    @Test
    void given_valid_match_should_not_filter(){
        List<User> matches = userService.filterMatches(validUser(), new ArrayList<>(Arrays.asList(validMatch())));
        assertFalse(matches.isEmpty());
    }

    @Test
    void given_blocked_valid_match_should_filter(){
        User user = validUser();
        user.getBlocked().add(validMatch().getId());
        List<User> matches = userService.filterMatches(user, new ArrayList<>(Arrays.asList(validMatch())));
        assertTrue(matches.isEmpty());
    }

    @Test
    void given_match_that_blocked_main_user_should_filter(){
        User match = validMatch();
        match.getBlocked().add(validUser().getId());
        List<User> matches = userService.filterMatches(validUser(), new ArrayList<>(Arrays.asList(match)));
        assertTrue(matches.isEmpty());
    }


    User validUser() {
        return User.builder()
                .id("mainUser")
                .biography("ValidBioWith20CharactersForTesting")
                .email("mainUser@testing.te")
                .image("imageUrl")
                .matches(new ArrayList<>())
                .blocked(new ArrayList<>())
                .tracks(new ArrayList<>(Arrays.asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .country("EE")
                .build();
    }

    User validMatch() {
        return User.builder()
                .id("validMatch")
                .biography("ValidBioWith20CharactersForTesting")
                .email("validMatch@testing.te")
                .image("imageUrl")
                .matches(new ArrayList<>())
                .blocked(new ArrayList<>())
                .tracks(new ArrayList<>(Arrays.asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .country("EE")
                .build();
    }

    User userWithEmptyBio() {
        return User.builder()
                .id("emptyBioUser")
                .biography("")
                .email("emptyBio@testing.te")
                .image("imageUrl")
                .matches(new ArrayList<>())
                .blocked(new ArrayList<>())
                .tracks(new ArrayList<>(Arrays.asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .country("EE")
                .build();
    }
}