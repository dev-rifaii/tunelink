package perosnal.spotifymatcher.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import perosnal.spotifymatcher.model.GetToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyAuthorization {
    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;

    private static final String REDIRECT = "/callback";

    private static final String BASE_URL = "https://accounts.spotify.com";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Autowired
    private ObjectMapper objectMapper;


    public String authenticationUrl(String baseRoute) {

        String uri = BASE_URL + "/authorize";

        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString(uri)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", baseRoute + REDIRECT)
                .queryParam("scope", "user-read-private user-read-email user-follow-read user-top-read");
        System.out.println(baseRoute);
        System.out.println(uriComponents.toUriString());
        return uriComponents.toUriString();
    }

    @SneakyThrows
    public String callback(String code, String baseRoute) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("redirect_uri", baseRoute + REDIRECT);
        parameters.put("grant_type", "authorization_code");

        String parametersString = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
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

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 400) {
            return "Error";
        }
        GetToken token = objectMapper.readValue(response.body(), GetToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);

        return objectMapper.writeValueAsString(token);
    }

    @SneakyThrows
    public String refreshToken(String refreshToken) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("refresh_token", refreshToken);
        parameters.put("grant_type", "refresh_token");

        String parametersString = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
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

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 400) {
            return "Error";
        }
        GetToken token = objectMapper.readValue(response.body(), GetToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);
        return objectMapper.writeValueAsString(token);
    }

}
