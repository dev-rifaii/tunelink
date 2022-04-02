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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> match(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        List<User> matches = userRepository.getMatches(user.getId(), 3);
        if (matchesAreNew(user, matches)) {
            user.getMatches().addAll(matches.stream().map(User::getId).collect(Collectors.toList()));
            userRepository.save(user);
            return matches;
        }
        return null;
    }


    public boolean matchesAreNew(User user, List<User> matches) {
        if (user.getMatches().stream().anyMatch(matches.stream().map(User::getId).collect(Collectors.toSet())::contains)) {
            return false;
        }
        return true;
    }

    public List<User> getMatches(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        List<User> matches = new ArrayList<>();
        for (String match : user.getMatches()) {
            matches.add(userRepository.findById(match).get());
        }
        return matches;
    }


    public List<String> getTopTracks(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getTracks();

    }


}
