package perosnal.spotifymatcher.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import perosnal.spotifymatcher.api.SpotifyAPI;
import perosnal.spotifymatcher.model.Token;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;
import perosnal.spotifymatcher.security.SpotifyAuthentication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final SpotifyAuthentication spotifyAuthentication;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SpotifyAPI spotifyAPI;

    public String authenticate() {
        return spotifyAuthentication.authenticate();
    }

    public Token callback(String code) throws URISyntaxException, IOException, InterruptedException {
        Token token = spotifyAuthentication.callback(code);
        persistUser(token);
        return token;
    }


    public void persistUser(Token token) throws IOException, URISyntaxException, InterruptedException {
        User user = fetchUserFromSpotifyApi(token);
        userRepository.save(user);
    }

    public User fetchUserFromSpotifyApi(Token token) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userNode = objectMapper.readValue(spotifyAPI.getProfile(token.getAccessToken()), ObjectNode.class);
        JsonNode topTracksNode = objectMapper.readTree(spotifyAPI.getTopTracks(token.getAccessToken())).get("items");
        JsonNode topArtistsNode = objectMapper.readTree(spotifyAPI.getTopArtists(token.getAccessToken())).get("items");
        token.setId(userNode.get("id").asText());

        User user = User.builder()
                .id(userNode.get("id").asText())
                .country(userNode.get("country").asText())
                .email(userNode.get("email").asText())
                .tracks(extractItemsFromJsonNode(topTracksNode))
                .artists(extractItemsFromJsonNode(topArtistsNode))
                .token(token)
                .build();
        return user;
    }

    public List<String> extractItemsFromJsonNode(JsonNode node) {
        List<String> items = new ArrayList<>();
        for (JsonNode item : node) {
            items.add(item.get("id").asText());
        }
        return items;
    }


}
