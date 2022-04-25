package perosnal.spotifymatcher.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import perosnal.spotifymatcher.model.Token;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpotifyAuthentication {

    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;

    private static final String REDIRECT = "http://localhost:8080/spotifymatcher/authentication/callback/";
//        private static final String REDIRECT = "http://localhost:8080/";

    private static final String BASE_URL = "https://accounts.spotify.com";
    private final ObjectMapper objectMapper;

    public String authenticate() {

        String uri = BASE_URL + "/authorize";

        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString(uri)
                .queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("scope", "user-read-private user-read-email user-follow-read user-top-read")
                .queryParam("redirect_uri", REDIRECT);

        return uriComponents.toUriString();
    }


    public Token callback(String code) throws URISyntaxException, IOException, InterruptedException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("redirect_uri", REDIRECT);
        parameters.put("grant_type", "authorization_code");

        String parametersString = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String beforeEncoding = CLIENT_ID + ":" + CLIENT_SECRET;
        String afterEncoding = Base64.getEncoder().encodeToString(beforeEncoding.getBytes(StandardCharsets.UTF_8));
        String header = "Basic " + afterEncoding;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/token"))
                .header("Authorization", header)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(parametersString))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode node = objectMapper.readTree(response.body());

        return Token.builder()
                .accessToken(node.get("access_token").asText())
                .refreshToken(node.get("refresh_token").asText())
                .build();
    }

}
