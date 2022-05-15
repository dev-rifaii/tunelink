package perosnal.spotifymatcher.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.model.Track;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.service.UserService;
import perosnal.spotifymatcher.util.AuthorizedActionResult;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:2000/")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("token") String token) {
        return ok(userService.getUser(token));
    }

    @GetMapping("/match")
    public ResponseEntity<?> match(@RequestHeader("token") String token) {
        return userService.match(token)
                .map(ResponseEntity::ok)
                .orElseGet(() -> badRequest().build());
    }

    @GetMapping("/tracksIds")
    public List<String> getTopTracksIds(@RequestHeader("token") String token) {
        return userService.getTopTracks(token);
    }

    @GetMapping("/tracks")
    public List<Track> getTopTracksDetailed(@RequestHeader("token") String token) {
        return userService.getTracksDetails(token);
    }
    @GetMapping("/matches")
    public List<User> getMatches(@RequestHeader("token") String token) {
        return userService.getMatches(token);
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestHeader("token") String token, @RequestBody String id) {
        userService.blockUser(token, id);
        return noContent().build();
    }


    @PostMapping("/bio")
    public ResponseEntity<?> setBio(@RequestHeader("token") String token, @RequestBody String bio) {
        if (userService.setBio(token, bio) == AuthorizedActionResult.SUCCESS) {
            return noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
