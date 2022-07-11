package perosnal.spotifymatcher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.TrackRepository;
import perosnal.spotifymatcher.repository.UserRepository;
import perosnal.spotifymatcher.security.JwtTokenValidator;
import perosnal.spotifymatcher.util.AuthorizedActionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TrackRepository trackRepository = Mockito.mock(TrackRepository.class);
    private final FakeUserService fakeUserService = Mockito.mock(FakeUserService.class);
    private final JwtTokenValidator jwtTokenValidator = Mockito.mock(JwtTokenValidator.class);
    private final UserService userService = new UserService(userRepository, trackRepository, fakeUserService, jwtTokenValidator);


    @ParameterizedTest(name = "given bio{0} setBio should return {1}")
    @MethodSource("givenBioToExpectedSetBioResult")
    void setBio_should_return_expectedResult(final String bio, final AuthorizedActionResult expectedResult) {
        when(jwtTokenValidator.getUserSpotifyId("")).thenReturn("");
        when(userRepository.getById("")).thenReturn(new User());

        final AuthorizedActionResult actualResult = userService.setBio("", bio);

        assertEquals(actualResult, expectedResult);
    }

    static Stream<Arguments> givenBioToExpectedSetBioResult() {
        return Stream.of(
                arguments("", AuthorizedActionResult.FAILURE),
                arguments("too short", AuthorizedActionResult.FAILURE),
                arguments(null, AuthorizedActionResult.FAILURE),
                arguments("123456789Test123456789", AuthorizedActionResult.SUCCESS)
        );
    }


    @ParameterizedTest(name = "given existing match {0} and blocked user {1}, if user is blocked by match={2} and match in filter is {3} result should be empty? {4}")
    @MethodSource("givenMatchesToExpectedFilterMatchesResult")
    void filterMatches_should_return_expectedResult(
            final User givenExistingMatch,
            final User givenBlockedUser,
            final boolean givenUserBlockedByMatch,
            final User matchInFilter,
            final boolean expectedEmpty
    ) {

        final User user = validUser();
        user.setMatches(givenExistingMatch == null ? emptyList() : singletonList(givenExistingMatch.getId()));
        user.setBlocked(givenBlockedUser == null ? emptyList() : singletonList(givenBlockedUser.getId()));
        if (givenUserBlockedByMatch) matchInFilter.setBlocked(singletonList(user.getId()));

        List<User> filteredMatches = userService.filterMatches(user, singletonList(matchInFilter));

        assertEquals(expectedEmpty, filteredMatches.isEmpty());
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

    static User validMatch() {
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

    static User userWithEmptyBio() {
        return User.builder()
                .id("emptyBioUser")
                .email("emptyBio@testing.te")
                .image("imageUrl")
                .matches(new ArrayList<>())
                .blocked(new ArrayList<>())
                .tracks(new ArrayList<>(Arrays.asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .country("EE")
                .build();
    }


    static Stream<Arguments> givenMatchesToExpectedFilterMatchesResult() {
        return Stream.of(
                arguments(validMatch(), null, false, validMatch(), true),
                arguments(null, null, false, userWithEmptyBio(), true),
                arguments(null, null, false, validMatch(), false),
                arguments(null, validMatch(), false, validMatch(), true),
                arguments(validMatch(), null, true, validMatch(), true),
                arguments(null, null, true, validMatch(), true)

        );
    }

    @Test
    void given_populated_list_of_matches_return_its_optional() {
        when(jwtTokenValidator.getUserSpotifyId("")).thenReturn(validUser().getId());
        when(userRepository.getById(validUser().getId())).thenReturn(validUser());
        User match = validMatch();
        when(userRepository.getMatches(validUser().getId(), 3)).thenReturn(singletonList(match));
        assertEquals(Optional.of(singletonList(match)), userService.match(""));
    }

    @Test
    void given_user_with_empty_bio_match_should_return_empty() {
        when(jwtTokenValidator.getUserSpotifyId("")).thenReturn(userWithEmptyBio().getId());
        when(userRepository.getById(userWithEmptyBio().getId())).thenReturn(userWithEmptyBio());
        when(userRepository.getMatches(userWithEmptyBio().getId(), 3)).thenReturn(singletonList(validMatch()));
        assertEquals(Optional.empty(), userService.match(""));
    }

    @Test
    void given_user_with_no_matches_return_optional_of_fake_generated_users() {
        when(jwtTokenValidator.getUserSpotifyId("")).thenReturn(validUser().getId());
        when(userRepository.getById(validUser().getId())).thenReturn(validUser());
        when(userRepository.getMatches(validUser().getId(), 3)).thenReturn(emptyList());
        List<User> fakeMatches = fakeUserService.generateUsers(validUser(), 3);
        assertEquals(Optional.of(fakeMatches), userService.match(""));
    }
}