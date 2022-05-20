package perosnal.spotifymatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.api.SpotifyAuthorization;
import perosnal.spotifymatcher.service.SpotifyApiService;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;


@RequestMapping("/authentication")
@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:2000/")
public class AuthenticationController {

    private final SpotifyApiService spotifyApiService;
    private final SpotifyAuthorization spotifyAuthorization;

    @GetMapping("/url")
    public String getUrl(@RequestHeader String baseRoute) {
        return spotifyAuthorization.authenticationUrl(baseRoute);
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader String code, @RequestHeader String baseRoute) {
        String response = spotifyAuthorization.callback(code, baseRoute);
        if (response.equals("Error")) {
            return badRequest().build();
        }
        return ok(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> getToken(@RequestHeader String refreshToken) {
        String response = spotifyAuthorization.refreshToken(refreshToken);
        if (response.equals("Error")) {
            return badRequest().build();
        }
        return ok(response);
    }
    @PostMapping("/persist")
    public ResponseEntity<?> persist(@RequestHeader("token") String token) {
        spotifyApiService.persistUser(token);
        return ResponseEntity.ok().build();
    }


}
