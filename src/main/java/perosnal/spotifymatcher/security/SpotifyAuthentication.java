package perosnal.spotifymatcher.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SpotifyAuthentication {

    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;
    private static final String REDIRECT = "http://localhost:8080/spotifymatcher/test/callback/";
    private ObjectMapper objectMapper = new ObjectMapper();

    public String authenticate() {

        String uri = "https://accounts.spotify.com/authorize";

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

        String f = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String beforeEncoding = CLIENT_ID + ":" + CLIENT_SECRET;
        String afterEncoding = Base64.getEncoder().encodeToString(beforeEncoding.getBytes(StandardCharsets.UTF_8));
        String header = "Basic " + afterEncoding;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://accounts.spotify.com/api/token"))
                .header("Authorization", header)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(f))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode node = objectMapper.readTree(response.body());
        Token token = Token.builder()
                .accessToken(node.get("access_token").asText())
                .refreshToken(node.get("refresh_token").asText())
                .build();
        return token;
    }

}
