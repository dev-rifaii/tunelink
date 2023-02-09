package perosnal.tunelink.spotify;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import perosnal.tunelink.spotify.dto.TokenRequestDto;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "Spotify-Authorization-API", url = "https://accounts.spotify.com")
public interface SpotifyAuthorizationClient {

    @RequestMapping(method = POST, value = "/api/token", produces = "application/x-www-form-urlencoded")
    SpotifyToken getToken(TokenRequestDto tokenRequestDto);

}
