package perosnal.spotifymatcher.service;


import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import perosnal.spotifymatcher.TestToken;
import perosnal.spotifymatcher.model.Token;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;

import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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