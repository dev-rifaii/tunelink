package perosnal.tunelink.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.tunelink.util.AuthorizedActionResult;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("jwt") String jwt) {
        return ok(userService.getUser(jwt));
    }

    @GetMapping("/match")
    public List<User> match(@RequestHeader("jwt") String jwt) {
        return userService.match(jwt);
    }

    @GetMapping("/tracks")
    public ResponseEntity<?> getTopTracksDetailed(@RequestHeader("jwt") String jwt) {
        return ok(userService.getTracksDetails(jwt));
    }

    @GetMapping("/matches")
    public ResponseEntity<?> getMatches(@RequestHeader("jwt") String jwt) {
        return ok(userService.getMatches(jwt));
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestHeader("jwt") String jwt, @RequestBody String id) {
        userService.blockUser(jwt, id);
        return noContent().build();
    }

    @PostMapping("/bio")
    public ResponseEntity<?> setBio(@RequestHeader("jwt") String jwt, @RequestBody String bio) {
        if (userService.setBio(jwt, bio) == AuthorizedActionResult.SUCCESS) {
            return noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/force")
    public ResponseEntity<?> forceMatch(@RequestBody ForcedMatchDto forcedMatchDto) {
        if (forcedMatchDto.adminPassword().equals(ADMIN_PASSWORD)) {
            userService.forceMatch(forcedMatchDto.firstId(), forcedMatchDto.secondId());
            return ok("Success");
        }
        return badRequest().build();
    }
}
