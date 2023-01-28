package perosnal.spotifymatcher.interceptors;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class SpotifyApiInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer ");
            requestTemplate.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        };
    }

}
