package perosnal.spotifymatcher.api;

import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.util.HttpRequestSender;


import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class SpotifyAPI {

    /* Reference:
    https://developer.spotify.com/documentation/web-api/reference/#/ */
    public static final String GET_PROFILE = "https://api.spotify.com/v1/me";
    public static final String GET_TOP_TRACKS = "https://api.spotify.com/v1/me/top/tracks/";
    public static final String GET_TOP_ARTISTS = "https://api.spotify.com/v1/me/top/artists";

    HttpRequestSender httpRequestSender = new HttpRequestSender();

    public String getProfile(String token) throws IOException, InterruptedException, URISyntaxException {
        return httpRequestSender.request(GET_PROFILE, token);
    }


    public String getTopTracks(String token) throws URISyntaxException, IOException, InterruptedException {
        return httpRequestSender.request(GET_TOP_TRACKS, token);
    }

    public String getTopArtists(String token) throws URISyntaxException, IOException, InterruptedException {
        return httpRequestSender.request(GET_TOP_ARTISTS, token);
    }

}
