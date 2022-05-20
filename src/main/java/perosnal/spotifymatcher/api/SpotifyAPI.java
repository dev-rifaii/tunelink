package perosnal.spotifymatcher.api;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.model.GetSeveralTracksResponse;
import perosnal.spotifymatcher.model.GetSpotifyTopItemsResponse;
import perosnal.spotifymatcher.model.GetSpotifyProfileResponse;
import perosnal.spotifymatcher.util.HttpRequestSender;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class SpotifyAPI {

    /**
     * See <a href="https://developer.spotify.com/documentation/web-api/reference/#/">reference documentation</a> for more details.
     */
    public static final String BASE_URL = "https://api.spotify.com/v1";


    @Autowired
    private final HttpRequestSender httpRequestSender;

    @SneakyThrows
    public GetSpotifyProfileResponse getProfile(String token) {
        return httpRequestSender.request(BASE_URL + "/me", token, GetSpotifyProfileResponse.class);
    }

    @SneakyThrows
    public List<String> getTopTracksId(String token) {
        return httpRequestSender.request(BASE_URL + "/me/top/tracks", token, GetSpotifyTopItemsResponse.class)
                .items()
                .stream()
                .map(GetSpotifyTopItemsResponse.SpotifyItem::id)
                .collect(toList());
    }

    @SneakyThrows
    public List<String> getTopArtistsId(String token) {
        return httpRequestSender.request(BASE_URL + "/me/top/artists", token, GetSpotifyTopItemsResponse.class)
                .items()
                .stream()
                .map(GetSpotifyTopItemsResponse.SpotifyItem::id)
                .collect(toList());
    }

    @SneakyThrows
    public GetSeveralTracksResponse getTracksDetails(List<String> tracksIds, String token) {
        String queryParam = "?ids=" + String.join(",", tracksIds);
        return httpRequestSender.request(BASE_URL + "/tracks/" + queryParam, token, GetSeveralTracksResponse.class);
    }

}
