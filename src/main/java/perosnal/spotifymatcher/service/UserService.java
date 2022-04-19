package perosnal.spotifymatcher.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.api.SpotifyAPI;
import perosnal.spotifymatcher.repository.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> match(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        List<User> matches = filterMatches(user, userRepository.getMatches(user.getId(), 3));
        if (user.getBiography()==null) {
            return null;
        }
        if (matches.size() > 0) {
            user.getMatches().addAll(matches.stream().map(User::getId).collect(Collectors.toList()));
            userRepository.save(user);
            return matches;
        }
        return Collections.emptyList();
    }

    public void blockUser(String accessToken, String id) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        user.getBlocked().add(id);
        userRepository.save(user);
    }


    public boolean setBio(String token, String bio) {
        User user = userRepository.findByTokenAccessToken(token);
        if (bio.length() > 20) {
            user.setBiography(bio);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> filterMatches(User user, List<User> matches) {
        return matches.stream()
                .filter(match -> !user.getMatches().contains(match.getId())
                        && !user.getBlocked().contains(match.getId())
                        && !match.getBlocked().contains(user.getId())
                        && match.getBiography()!=null)
                .collect(Collectors.toList());

    }

    public List<User> getMatches(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        List<User> matches = new ArrayList<>();
        user.getMatches()
                .forEach(match -> matches.add(userRepository.findById(match).get()));
        return matches;
    }


    public List<String> getTopTracks(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getTracks();
    }


}
