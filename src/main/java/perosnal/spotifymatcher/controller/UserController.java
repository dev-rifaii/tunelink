package perosnal.spotifymatcher.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.service.UserService;
import perosnal.spotifymatcher.util.AuthorizedActionResult;

import java.util.Collections;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
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
                .orElseGet(() ->ok(Collections.emptyList()));
    }

    @GetMapping("/tracksIds")
    public ResponseEntity<?> getTopTracksIds(@RequestHeader("token") String token) {
        return ok(userService.getTopTracks(token));
    }

    @GetMapping("/tracks")
    public ResponseEntity<?> getTopTracksDetailed(@RequestHeader("token") String token) {
        return ok(userService.getTracksDetails(token));
    }

    @GetMapping("/matches")
    public ResponseEntity<?> getMatches(@RequestHeader("token") String token) {
        return ok(userService.getMatches(token));
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestHeader("token") String token, @RequestBody String id) {
        userService.blockUser(token, id);
        return noContent().build();
    }


    @PostMapping("/bio")
    public ResponseEntity<?> setBio(@RequestHeader("token") String token, @RequestBody String bio) {
        System.out.println("this is bio " + bio);
        if (userService.setBio(token, bio) == AuthorizedActionResult.SUCCESS) {
            return noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
