package perosnal.spotifymatcher.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/match")
    public List<User> match(@RequestHeader("Authorization") String token) {
        return userService.match(token.substring(7));
    }

    @GetMapping("/tracks")
    public List<String> getTopTracks(@RequestHeader("Authorization") String token) throws IOException, URISyntaxException, InterruptedException {
        return userService.getTopTracks(token.substring(7));
    }

    @GetMapping("/matches")
    public List<User> getMatches(@RequestHeader("Authorization") String token) throws IOException, URISyntaxException, InterruptedException {
        return userService.getMatches(token.substring(7));
    }
}
