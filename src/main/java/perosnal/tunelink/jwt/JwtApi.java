package perosnal.tunelink.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1")
public class JwtApi {

    @Autowired
    private JwtManager jwtManager;

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader String userId) {
        String token = jwtManager.generateToken(userId);
        return ok(token);
    }
}
