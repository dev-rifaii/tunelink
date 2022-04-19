package perosnal.spotifymatcher.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/match")
    public ResponseEntity<?> match(@RequestHeader("Authorization") String token) {
        List<User> matches = userService.match(token.substring(7));
        if (matches == null) {
            return badRequest().build();
        }
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/tracks")
    public List<String> getTopTracks(@RequestHeader("Authorization") String token) throws IOException, URISyntaxException, InterruptedException {
        return userService.getTopTracks(token.substring(7));
    }

    @GetMapping("/matches")
    public List<User> getMatches(@RequestHeader("Authorization") String token) throws IOException, URISyntaxException, InterruptedException {
        return userService.getMatches(token.substring(7));
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestHeader("Authorization") String token, @RequestBody String id) {
        userService.blockUser(token.substring(7), id);
        return noContent().build();
    }

    @PostMapping("/bio")
    public ResponseEntity<?> setBio(@RequestHeader("Authorization") String token, @RequestBody String bio) {
        if (userService.setBio(token.substring(7), bio)) {
            return noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
