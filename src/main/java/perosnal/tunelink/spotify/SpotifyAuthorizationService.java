package perosnal.tunelink.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import perosnal.tunelink.util.HttpRequestSender;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpotifyAuthorizationService {
    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;

    private static final String REDIRECT = "/api/v1/auth/callback";

    private static final String BASE_URL = "https://accounts.spotify.com";

    public static final String SCOPE = "user-read-email user-top-read";

    private final HttpRequestSender httpRequestSender;
    private final ObjectMapper objectMapper;
    private final SpotifyAuthorizationClient authorizationClient;
    private final SpotifyApiService spotifyApiService;

    public String authenticationUrl(String baseRoute) {
        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString(BASE_URL + "/authorize")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", baseRoute + REDIRECT)
                .queryParam("scope", SCOPE);
        return uriComponents.toUriString();
    }

    //TODO refactor to openfeign
    @SneakyThrows
    public String callback(String code, String baseRoute) {

        String parametersString = "code=" + code
                + "&redirect_uri=" + baseRoute + REDIRECT
                + "&grant_type=authorization_code";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/token"))
                .header("Authorization", getEncodedSpotifyAppCredentialsHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(parametersString))
                .build();

        HttpResponse<String> response = httpRequestSender.genericRequest(httpRequest);
        if (response.statusCode() == 400) {
            log.warn(response.body());
            return "Error";
        }
        SpotifyToken token = objectMapper.readValue(response.body(), SpotifyToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);

        spotifyApiService.persistUser(token.access_token);

        return objectMapper.writeValueAsString(token);
    }

    @SneakyThrows
    public String refreshToken(String refreshToken) {

        String parametersString = "refresh_token=" + refreshToken
                + "&grant_type=refresh_token";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/token"))
                .header("Authorization", getEncodedSpotifyAppCredentialsHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(parametersString))
                .build();

        HttpResponse<String> response = httpRequestSender.genericRequest(httpRequest);
        if (response.statusCode() == 400) {
            return "Error";
        }
        SpotifyToken token = objectMapper.readValue(response.body(), SpotifyToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);
        return objectMapper.writeValueAsString(token);
    }

    private String getEncodedSpotifyAppCredentialsHeader() {
        String beforeEncoding = CLIENT_ID + ":" + CLIENT_SECRET;
        return "Basic " + Base64.getEncoder().encodeToString(beforeEncoding.getBytes(StandardCharsets.UTF_8));
    }

}
