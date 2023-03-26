package perosnal.tunelink.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader String userId) {
        return ok(userService.getUser(userId));
    }

    @GetMapping("/match")
    public List<User> match(@RequestHeader String userId) {
        return userService.match(userId);
    }

    @GetMapping("/tracks")
    public ResponseEntity<?> getTopTracksDetailed(@RequestHeader String userId) {
        return ok(userService.getTracksDetails(userId));
    }

    @GetMapping("/matches")
    public ResponseEntity<?> getMatches(@RequestHeader String userId) {
        return ok(userService.getMatches(userId));
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestHeader String userId, @RequestBody String id) {
        userService.blockUser(userId, id);
        return noContent().build();
    }

    @PostMapping("/bio")
    public void setBio(@RequestHeader String userId, @RequestBody String bio) {
        userService.updateBiography(userId, bio);
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
