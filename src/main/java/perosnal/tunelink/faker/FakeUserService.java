package perosnal.tunelink.faker;

import org.springframework.stereotype.Service;
import perosnal.tunelink.user.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeUserService {

    final Random random = new Random();

    public List<User> generateUsers(User user, int amount) {
        return IntStream.range(0, amount)
                .mapToObj(it -> generateUser(user))
                .collect(Collectors.toList());
    }

    public User generateUser(User user) {
        return User.builder()
                .id(user.getId() + random.nextInt())
                .country(user.getCountry())
                .tracks(user.getTracks().stream().limit(3).collect(Collectors.toList()))
                .email("fake" + random.nextInt() + "@generated.fk")
                .imageUrl("https://i.pravatar.cc/300?u=" + random.nextInt())
                .biography("This user was generated for testing purposes")
                .build();
    }

}
