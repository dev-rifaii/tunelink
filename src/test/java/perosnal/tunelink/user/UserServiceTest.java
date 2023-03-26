package perosnal.tunelink.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import perosnal.tunelink.exceptions.TuneLinkException;
import perosnal.tunelink.faker.FakeUserService;
import perosnal.tunelink.track.TrackRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TrackRepository trackRepository = Mockito.mock(TrackRepository.class);
    private final FakeUserService fakeUserService = Mockito.mock(FakeUserService.class);
    private final UserService userService = new UserService(userRepository, trackRepository, fakeUserService);

    private final String VALID_USER_ID = validUser().getId();

    @Test
    void updateBiography_ThrowsTuneLinkException_GivenNullBio() {
        when(userRepository.getById(VALID_USER_ID)).thenReturn(new User());

        assertThrows(
                TuneLinkException.class,
                () -> userService.updateBiography(VALID_USER_ID, null),
                "Biography should be more than 20 characters"
        );
    }

    @Test
    void updateBiography_ThrowsTuneLinkException_GivenTooShortBio() {
        when(userRepository.getById(VALID_USER_ID)).thenReturn(new User());

        assertThrows(
                TuneLinkException.class,
                () -> userService.updateBiography(VALID_USER_ID, "12345"),
                "Biography should be more than 20 characters"
        );
    }

    @Test
    void updateBiography_IsSuccessful_GivenValidBio() {
        String biography = "TwentyCharactersBiographyTesting";

        when(userRepository.getById(VALID_USER_ID)).thenReturn(new User());

        userService.updateBiography(VALID_USER_ID, biography);

        verify(userRepository, times(1)).updateBiography(VALID_USER_ID, biography);
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
        return new User()
                .setId("mainUser")
                .setBiography("ValidBioWith20CharactersForTesting")
                .setEmail("mainUser@testing.te")
                .setImageUrl("imageUrl")
                .setMatches(new ArrayList<>())
                .setBlocked(new ArrayList<>())
                .setTracks(new ArrayList<>(asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .setCountry("EE");
    }

    static User validMatch() {
        return new User()
                .setId("validMatch")
                .setBiography("ValidBioWith20CharactersForTesting")
                .setEmail("validMatch@testing.te")
                .setImageUrl("imageUrl")
                .setMatches(new ArrayList<>())
                .setBlocked(new ArrayList<>())
                .setTracks(new ArrayList<>(asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .setCountry("EE");
    }

    static User userWithEmptyBio() {
        return new User()
                .setId("emptyBioUser")
                .setEmail("emptyBio@testing.te")
                .setImageUrl("imageUrl")
                .setMatches(new ArrayList<>())
                .setBlocked(new ArrayList<>())
                .setTracks(new ArrayList<>(asList("Track1", "Track2", "Track3", "Track4", "Track5")))
                .setCountry("EE");
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
        when(userRepository.getById(VALID_USER_ID)).thenReturn(validUser());
        User match = validMatch();
        when(userRepository.getMatches(VALID_USER_ID, 3)).thenReturn(singletonList(match));
        assertEquals(singletonList(match), userService.match(VALID_USER_ID));
    }

    @Test
    void given_user_with_empty_bio_match_should_return_empty() {
        var userId = userWithEmptyBio().getId();
        when(userRepository.getById(userId)).thenReturn(userWithEmptyBio());
        when(userRepository.getMatches(userId, 3)).thenReturn(singletonList(validMatch()));
        assertThrows(TuneLinkException.class, () -> userService.match(userId));
    }

    @Test
    void given_user_with_no_matches_return_optional_of_fake_generated_users() {
        when(userRepository.getById(VALID_USER_ID)).thenReturn(validUser());
        when(userRepository.getMatches(VALID_USER_ID, 3)).thenReturn(emptyList());
        List<User> fakeMatches = fakeUserService.generateUsers(validUser(), 3);
        assertEquals(fakeMatches, userService.match(VALID_USER_ID));
    }
}