package perosnal.tunelink.spotify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;


@RequestMapping("api/v1/spotify-auth")
@RequiredArgsConstructor
@RestController
public class SpotifyAuthApi {

    private final SpotifyApiService spotifyApiService;
    private final SpotifyAuthorizationService spotifyAuthorizationService;

    @GetMapping("/url")
    public String getUrl(@RequestParam String host) {
        return spotifyAuthorizationService.authenticationUrl(host);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        String domain = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return spotifyAuthorizationService.callback(code, domain);
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader String code, @RequestHeader String baseRoute) {
        String response = spotifyAuthorizationService.callback(code, baseRoute);
        return response.equals("error") ? badRequest().build() : ok(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> getToken(@RequestHeader String refreshToken) {
        String response = spotifyAuthorizationService.refreshToken(refreshToken);
        return response.equals("error") ? badRequest().build() : ok(response);
    }

    @PostMapping("/persist")
    public ResponseEntity<?> persist(@RequestHeader("token") String token) {
        spotifyApiService.persistUser(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id")
    public ResponseEntity<?> getId(@RequestHeader("token") String token) {
        return ok(spotifyApiService.getIdByToken(token));
    }


}
