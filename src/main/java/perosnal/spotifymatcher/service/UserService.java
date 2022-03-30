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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> match(String accessToken) {
        List<User> matches = new ArrayList<>();

        User user = userRepository.findById(userRepository.findByTokenAccessToken(accessToken).getId()).get();

        List<String> topTracks = user.getTracks();
        List<String> afterRetain = new ArrayList<>(topTracks);
        for (String track : topTracks) {
            for (User potentialMatchingUser : userRepository.findUsersBySharedTracks(track)) {
                if (!potentialMatchingUser.equals(user) && !matches.contains(potentialMatchingUser) && !user.getMatches().contains(potentialMatchingUser.getId())) {
                    afterRetain.retainAll(potentialMatchingUser.getTracks());
                    if (afterRetain.size() >= 1) {
                        matches.add(potentialMatchingUser);
                        user.getMatches().add(potentialMatchingUser.getId());
                    }
                }
            }
        }
        return matches;
    }


    public List<User> matchByTracksAndCountry(String accessToken) {
        List<User> matches = new ArrayList<>();

        User user = userRepository.findById(userRepository.findByTokenAccessToken(accessToken).getId()).get();

        List<User> locals = userRepository.findByCountry(user.getCountry());
        List<String> topTracks = user.getTracks();
        List<String> afterRetain = new ArrayList<>(topTracks);
        for (String track : topTracks) {
            for (User potentialMatchingUser : locals) {
                if (!potentialMatchingUser.equals(user) && !matches.contains(potentialMatchingUser) && !user.getMatches().contains(potentialMatchingUser.getId())) {
                    afterRetain.retainAll(potentialMatchingUser.getTracks());
                    if (afterRetain.size() >= 2) {
                        matches.add(potentialMatchingUser);
                        user.getMatches().add(potentialMatchingUser.getId());

                    }
                }
            }
        }

        return matches;

    }

    public String getId(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getId();
    }


    public String getEmail(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getEmail();
    }

    public String getCountry(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getCountry();
    }

    public List<String> getTopTracks(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getTracks();

    }

    public List<String> getTopArtists(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getArtists();
    }

    public List<String> getMatches(String accessToken) {
        User user = userRepository.findByTokenAccessToken(accessToken);
        return user.getMatches();
    }


}
