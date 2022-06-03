package perosnal.spotifymatcher.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import perosnal.spotifymatcher.model.SpotifyUser;
import perosnal.spotifymatcher.model.Track;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;
import perosnal.spotifymatcher.util.AuthorizedActionResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SpotifyApiService spotifyApiService;

    private final FakeUserService fakeUserService;

    public Optional<List<User>> match(String accessToken) {
        User user = userRepository.getById(spotifyApiService.getIdByToken(accessToken));
        List<User> matches = filterMatches(user, userRepository.getMatches(user.getId(), 3));
        if (user.getBiography() == null) {
            return Optional.empty();
        }
        if (matches.size() > 0) {
            user.getMatches()
                    .addAll(matches.stream()
                            .map(User::getId)
                            .toList());
            userRepository.save(user);
            return Optional.of(matches);
        }
        return Optional.of(fakeUserService.generateUsers(user, 3));
    }


    public User getUser(String accessToken) {
        SpotifyUser user = spotifyApiService.fetchUserFromSpotifyApi(accessToken);
        return userRepository.getById(user.profile()
                .id());
    }

    public void blockUser(String accessToken, String id) {
        User user = userRepository.getById(spotifyApiService.getIdByToken(accessToken));
        user.getBlocked()
                .add(id);
        userRepository.save(user);
    }


    public AuthorizedActionResult setBio(String accessToken, String bio) {
        User user = userRepository.getById(spotifyApiService.getIdByToken(accessToken));
        if (bio.length() > 20) {
            user.setBiography(bio);
            userRepository.save(user);
            return AuthorizedActionResult.SUCCESS;
        }
        return AuthorizedActionResult.FAILURE;
    }

    public List<User> filterMatches(User user, List<User> matches) {
        return matches.stream()
                .filter(match -> !user.getMatches()
                        .contains(match.getId())
                        && !user.getBlocked()
                        .contains(match.getId())
                        && !match.getBlocked()
                        .contains(user.getId())
                        && match.getBiography() != null)
                .collect(Collectors.toList());

    }

    public List<User> getMatches(String accessToken) {
        return userRepository.getById(spotifyApiService.getIdByToken(accessToken))
                .getMatches()
                .stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Track> getTracksDetails(String accessToken) {
        User user = userRepository.getById(spotifyApiService.getIdByToken(accessToken));
        return spotifyApiService.getTracksDetails(user.getTracks(), accessToken);
    }

    public List<String> getTopTracks(String accessToken) {
        return userRepository.getById(spotifyApiService.getIdByToken(accessToken))
                .getTracks();
    }


}
