package perosnal.spotifymatcher.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import perosnal.spotifymatcher.model.GetSeveralTracksResponse;
import perosnal.spotifymatcher.model.GetSpotifyProfileResponse;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * See <a href="https://developer.spotify.com/documentation/web-api/reference/#/">reference documentation</a> for more details.
 */
@FeignClient(name = "Spotify-API", url = "https://api.spotify.com/v1")
public interface SpotifyApiClient {

    @RequestMapping(method = GET, value = "/me")
    GetSpotifyProfileResponse getProfile(@RequestHeader(name = "Authorization") String token);

    @RequestMapping(method = GET, value = "/me/top/tracks")
    List<String> getTopTracksId(@RequestHeader(name = "Authorization") String token);

    @RequestMapping(method = GET, value = "/me/top/tracks")
    List<String> getTopArtistsId(@RequestHeader(name = "Authorization") String token);

    @RequestMapping(method = GET, value = "/tracks/")
    GetSeveralTracksResponse getTracksDetails(@RequestHeader(name = "Authorization") String token, @RequestParam String ids);
}
