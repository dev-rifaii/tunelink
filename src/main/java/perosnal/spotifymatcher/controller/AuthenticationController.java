package perosnal.spotifymatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.service.SpotifyApiService;

@RequestMapping("/authentication")
@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:2000/")
public class AuthenticationController {

    private final SpotifyApiService spotifyAuthentication;

    @PostMapping("/persist")
    public ResponseEntity<?> persist(@RequestHeader("token") String token) {
        spotifyAuthentication.persistUser(token);
        return ResponseEntity.ok().build();
    }



}
