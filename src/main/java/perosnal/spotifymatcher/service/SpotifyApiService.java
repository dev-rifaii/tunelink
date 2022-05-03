package perosnal.spotifymatcher.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import perosnal.spotifymatcher.api.SpotifyAPI;
import perosnal.spotifymatcher.model.Track;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SpotifyApiService {


    private final UserRepository userRepository;
    private final SpotifyAPI spotifyAPI;
    private final ObjectMapper objectMapper;


    public void persistUser(String token) {
        User user = fetchUserFromSpotifyApi(token);
        userRepository.save(user);
    }

    @SneakyThrows
    public User fetchUserFromSpotifyApi(String token) {
        JsonNode userNode = objectMapper.readTree(spotifyAPI.getProfile(token));
        JsonNode topTracksNode = objectMapper.readTree(spotifyAPI.getTopTracksId(token)).get("items");
        JsonNode topArtistsNode = objectMapper.readTree(spotifyAPI.getTopArtistsId(token)).get("items");
        return User.builder()
                .id(userNode.get("id").asText())
                .country(userNode.get("country").asText())
                .email(userNode.get("email").asText())
                .image(userNode.get("images").get(0).get("url").asText())
                .tracks(extractItemsFromJsonNode(topTracksNode))
                .artists(extractItemsFromJsonNode(topArtistsNode))
                .build();
    }

    @SneakyThrows
    public String getIdByToken(String token) {
        JsonNode userNode = objectMapper.readTree(spotifyAPI.getProfile(token));
        return userNode.get("id").asText();

    }

    @SneakyThrows
    public List<Track> getTracksDetails(List<String> tracksIds, String token) {
        List<Track> tracks = new ArrayList<>();
        JsonNode topTracksNode = objectMapper.readTree(spotifyAPI.getTracksDetails(tracksIds, token)).get("tracks");
        for (JsonNode item : topTracksNode) {

            tracks.add(Track.builder()
                    .id(item.get("id").asText())
                    .href(item.get("external_urls").get("spotify").asText())
                    .name(item.get("name").asText())
                    .imageUrl(item.get("album").get("images").get(0).get("url").asText())
                    .build());
        }
        return tracks;
    }

    public List<String> extractItemsFromJsonNode(JsonNode node) {
        List<String> items = new ArrayList<>();
        for (JsonNode item : node) {
            items.add(item.get("id").asText());
        }
        return items;
    }


}
