package perosnal.tunelink.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Profile("local")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/jwt")
public class LocalJwtApi {

    private final JwtManager jwtManager;

    @GetMapping
    public ResponseEntity<?> getToken(@RequestHeader String userId) {
        String token = jwtManager.generateToken(userId);
        return ok(token);
    }
}
