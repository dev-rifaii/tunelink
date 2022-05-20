package perosnal.spotifymatcher.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.exception.InvalidTokenException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class HttpRequestSender {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T request(String url, String token, Class<T> responseClass) {
        return objectMapper.readValue(
                request(url, token),
                responseClass
        );
    }



    @SneakyThrows
    public String request(String url, String token) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==400 || response.statusCode()==401){
            throw new InvalidTokenException();
        }
        return response.body();
    }
}
