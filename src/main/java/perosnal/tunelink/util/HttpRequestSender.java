package perosnal.tunelink.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
//TODO: Use Feign instead of HttpRequestSender for Spotify auth flow
public class HttpRequestSender {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @SneakyThrows
    public HttpResponse<String> genericRequest(HttpRequest httpRequest) {
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
