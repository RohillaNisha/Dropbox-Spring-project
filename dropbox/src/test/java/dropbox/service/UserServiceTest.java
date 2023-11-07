package dropbox.service;

import dropbox.dao.UserDao;
import dropbox.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserServiceTest {

@Autowired
 UserDao userDao ;

@Test
    @DisplayName("Should pass when a new user is created")
    void testAddNewUser(){
    User newUser = new User();
    newUser.setFullName("test");
    newUser.setUserName("test");
    newUser.setPassword("test1212");


    UserService userService = new UserService(userDao);

    ResponseEntity<String> response = userService.addNewUser(newUser);

    Assertions.assertEquals("User created", response.getBody());
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

}

}