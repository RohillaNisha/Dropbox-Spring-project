package dropbox.service;

import dropbox.repository.UserRepository;
import dropbox.models.User;
import dropbox.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserServiceTest {

@Autowired
UserRepository userRepository;

@Test
    @DisplayName("Should pass when a new user is created")
    void testAddNewUser(){
    User newUser = new User();
    newUser.setFullName("test");
    newUser.setUsername("test");
    newUser.setPassword("test1212");

    UserService userService = new UserService(userRepository);

    ResponseEntity<String> response = userService.addNewUser(newUser);

    Assertions.assertEquals("User created", response.getBody());
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

}

}