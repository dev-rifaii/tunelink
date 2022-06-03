package perosnal.spotifymatcher.service;

import org.springframework.stereotype.Service;
import perosnal.spotifymatcher.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FakeUserService {

    Random random = new Random();

    public List<User> generateUsers(User user, int amount) {
        List<User> fakeUsers = new ArrayList();
        for (int i = 0; i < amount; i++) {
            fakeUsers.add(generateUser(user));
        }
        return fakeUsers;
    }

    public User generateUser(User user) {
        random.setSeed(System.currentTimeMillis());
        return User.builder()
                .id(user.getId() + random.nextInt())
                .country(user.getCountry())
                .tracks(user.getTracks().stream().limit(3).collect(Collectors.toList()))
                .email("fake@generated.fk")
                .image("")
                .biography("This user was generated for testing purposes")
                .build();
    }

}
