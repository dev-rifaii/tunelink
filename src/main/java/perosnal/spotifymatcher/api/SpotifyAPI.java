package perosnal.spotifymatcher.api;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.util.HttpRequestSender;

import java.util.List;

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
    public String getProfile(String token) {
        return httpRequestSender.request(BASE_URL + "/me", token);
    }

    @SneakyThrows
    public String getTopTracksId(String token) {
        return httpRequestSender.request(BASE_URL + "/me/top/tracks", token);
    }

    @SneakyThrows
    public String getTopArtistsId(String token) {
        return httpRequestSender.request(BASE_URL + "/me/top/artists", token);
    }

    @SneakyThrows
    public String getTracksDetails(List<String> tracksIds, String token) {
        String queryParam = "?ids=" + String.join(",", tracksIds);
        System.out.println(queryParam);
        return httpRequestSender.request(BASE_URL + "/tracks/" + queryParam, token);
    }

}
