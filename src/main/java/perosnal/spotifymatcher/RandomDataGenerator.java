package perosnal.spotifymatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.model.Token;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RandomDataGenerator implements CommandLineRunner {


    private final UserRepository userRepository;


    @Override
    public void run(String... args){
        insertRandomDataToDatabase(false);
    }

    public void insertRandomDataToDatabase(boolean start) {

        if (start) {
            List<String> tracks = tracks();
            List<String> names = names();

            for (String name : names) {
                String id = name + "_first_id";
                Token token = Token.builder()
                        .id(id)
                        .accessToken(name + "_first_access_token")
                        .refreshToken(name + "_first_refresh_token")
                        .build();

                User user = User.builder()
                        .id(id)
                        .email(name + "_first@email.com")
                        .country(countries().get((int) (Math.random() * countries().size())))
                        .tracks(randomTwentyTracks(tracks))
                        .token(token)
                        .build();

                userRepository.save(user);
            }
        }
    }


    public List<String> names() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            names.add(i + "Rgn" + i);
        }
        return names;
    }

    public List<String> countries() {
        return Arrays.asList("EE", "LB", "US", "NG", "PR", "EG", "FN", "PL", "RU", "PZ");
    }

    public List<String> tracks() {
        List<String> tracks = new ArrayList<>();
        for (int c = 1; c <= 300; c++) {
            tracks.add("track" + c);
        }
        return tracks;
    }


    public List<String> randomTwentyTracks(List<String> allTracks) {
        List<String> twentyTracks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            twentyTracks.add(allTracks.get((int) (Math.random() * allTracks.size())));
        }
        return twentyTracks;
    }
}
