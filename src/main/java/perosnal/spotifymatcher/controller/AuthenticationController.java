package perosnal.spotifymatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import perosnal.spotifymatcher.model.Token;
import perosnal.spotifymatcher.service.AuthenticationService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@RequestMapping("/test")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService spotifyAuthentication;

    @GetMapping("/auth")
    public void auth(HttpServletResponse response) throws IOException{
        response.sendRedirect(spotifyAuthentication.authenticate());
    }

    @GetMapping("/callback")
    public Token callback(@RequestParam String code) throws URISyntaxException, IOException, InterruptedException {
        return spotifyAuthentication.callback(code);
    }

}
