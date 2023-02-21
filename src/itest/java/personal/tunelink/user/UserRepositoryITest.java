package personal.tunelink.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import perosnal.tunelink.user.UserRepository;
import personal.tunelink.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserRepositoryITest extends IntegrationTestBase {

    @Autowired
    UserRepository userRepository;

    @Test
    void test() {
        assertNotNull(userRepository);
    }
}
