package perosnal.spotifymatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import perosnal.spotifymatcher.model.Token;
import perosnal.spotifymatcher.service.AuthenticationService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@RequestMapping("/authentication")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService spotifyAuthentication;
    private final HttpServletResponse response;

    @CrossOrigin
    @GetMapping("/auth")
    public void auth(HttpServletResponse response) throws IOException {
        response.sendRedirect(spotifyAuthentication.authenticate());
    }

    @CrossOrigin
    @GetMapping("/callback")
    public Token callback(@RequestParam String code, HttpServletResponse redResponse) throws URISyntaxException, IOException, InterruptedException {
        Token token = spotifyAuthentication.callback(code);
        response.setHeader("Set-Cookie", "Token=" + token.getAccessToken() + "; domain=localhost.com");
        redResponse.sendRedirect("http://localhost.com:3000/home");
        return token;
    }

}
