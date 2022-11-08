package perosnal.spotifymatcher;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SpotifyMatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyMatcherApplication.class, args);
    }


}
