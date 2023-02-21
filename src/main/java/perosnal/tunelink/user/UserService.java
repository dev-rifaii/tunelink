package perosnal.tunelink.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import perosnal.tunelink.track.Track;
import perosnal.tunelink.track.TrackRepository;
import perosnal.tunelink.jwt.JwtManager;
import perosnal.tunelink.faker.FakeUserService;
import perosnal.tunelink.util.AuthorizedActionResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static perosnal.tunelink.util.Assertions.isTrue;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final FakeUserService fakeUserService;
    private final JwtManager jwtManager;


    public List<User> match(String jwt) {
        jwtManager.validateJwt(jwt);
        User user = userRepository.getById(jwtManager.getUserSpotifyId(jwt));
        isTrue(user.getBiography() != null, "Biography needs to be set before matching");
        List<User> matches = filterMatches(user, userRepository.getMatches(user.getId(), 3));
        if (matches.size() > 0) {
            persistMatches(user, matches);
            return matches;
        }
        return fakeUserService.generateUsers(user, 3);
    }


    public User getUser(String jwt) {
        jwtManager.validateJwt(jwt);
        return userRepository.getById(jwtManager.getUserSpotifyId(jwt));
    }

    public void blockUser(String jwt, String id) {
        jwtManager.validateJwt(jwt);
        User user = userRepository.getById(jwtManager.getUserSpotifyId(jwt));
        user.getBlocked().add(id);
        userRepository.save(user);
    }


    public AuthorizedActionResult setBio(String jwt, String bio) {
        jwtManager.validateJwt(jwt);
        User user = userRepository.getById(jwt);
        if (bio != null && bio.length() > 20) {
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
                        && match.getBiography() != ""
                        && match.getBiography() != null
                )
                .collect(Collectors.toList());
    }

    public List<User> getMatches(String jwt) {
        jwtManager.validateJwt(jwt);
        return userRepository.getById(jwtManager.getUserSpotifyId(jwt))
                .getMatches()
                .stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Track> getTracksDetails(String jwt) {
        jwtManager.validateJwt(jwt);
        return userRepository.getById(jwtManager.getUserSpotifyId(jwt))
                .getTracks()
                .stream()
                .map(trackRepository::getById)
                .collect(Collectors.toList());
    }

    private void persistMatches(User user, List<User> newMatches) {
        user.getMatches()
                .addAll(newMatches.stream()
                        .map(User::getId)
                        .toList());
        newMatches.forEach(match -> {
            match.getMatches().add(user.getId());
            userRepository.save(match);
        });
        userRepository.save(user);
    }

    public void forceMatch(String firstId, String secondId) {
        User firstUser = userRepository.getById(firstId);
        User secondUser = userRepository.getById(secondId);
        firstUser.getMatches().add(secondId);
        secondUser.getMatches().add(firstId);
        userRepository.save(firstUser);
        userRepository.save(secondUser);
    }

}
