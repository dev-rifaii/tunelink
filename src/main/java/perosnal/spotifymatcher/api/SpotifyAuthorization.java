package perosnal.spotifymatcher.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import perosnal.spotifymatcher.model.GetToken;
import perosnal.spotifymatcher.util.HttpRequestSender;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class SpotifyAuthorization {
    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;

    private static final String REDIRECT = "/callback";

    private static final String BASE_URL = "https://accounts.spotify.com";

    public static final String SCOPE = "user-read-email user-top-read";

    private final HttpRequestSender httpRequestSender;

    private final ObjectMapper objectMapper;

    public SpotifyAuthorization(final HttpRequestSender httpRequestSender, final ObjectMapper objectMapper) {
        this.httpRequestSender = httpRequestSender;
        this.objectMapper = objectMapper;
    }


    public String authenticationUrl(String baseRoute) {

        String uri = BASE_URL + "/authorize";

        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString(uri)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", baseRoute + REDIRECT)
                .queryParam("scope", SCOPE);
        return uriComponents.toUriString();
    }

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
            log.info(response.body());
            return "Error";
        }
        GetToken token = objectMapper.readValue(response.body(), GetToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);

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
        GetToken token = objectMapper.readValue(response.body(), GetToken.class);
        token.setExpires_at(System.currentTimeMillis() + 3600 * 1000);
        return objectMapper.writeValueAsString(token);
    }

    private String getEncodedSpotifyAppCredentialsHeader() {
        String beforeEncoding = CLIENT_ID + ":" + CLIENT_SECRET;
        return "Basic " + Base64.getEncoder().encodeToString(beforeEncoding.getBytes(StandardCharsets.UTF_8));
    }

}
