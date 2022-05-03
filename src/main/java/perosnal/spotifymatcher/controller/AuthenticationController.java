package perosnal.spotifymatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perosnal.spotifymatcher.service.SpotifyApiService;

@RequestMapping("/authentication")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final SpotifyApiService spotifyAuthentication;

    @PostMapping("/persist")
    public ResponseEntity<?> persist(@RequestHeader("token") String token) {
        spotifyAuthentication.persistUser(token);
        return ResponseEntity.ok().build();
    }


}
