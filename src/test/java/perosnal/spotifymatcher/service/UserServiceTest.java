package perosnal.spotifymatcher.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import perosnal.spotifymatcher.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository);
    }

}