package dropbox.service;

import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.payloads.request.SignupRequest;
import dropbox.repository.RoleRepository;
import dropbox.repository.UserRepository;
import dropbox.models.User;
import dropbox.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserDetailsServiceImplTest {

@Autowired
UserRepository userRepository;
@Autowired
BCryptPasswordEncoder bCryptPasswordEncoder;

@Autowired
private RoleRepository roleRepository;

@Autowired
private UserDetailsServiceImpl userDetailsServiceImpl;


    @Test
    @DisplayName("Should pass when a new user is created")
    void testCreateAUser() throws UserAlreadyExistsException, UserNameCannotBeNullException , PasswordCannotBeNullException {
    SignupRequest newSignupUser = new SignupRequest();
    newSignupUser.setFullName("test");
    newSignupUser.setUsername("test");
    newSignupUser.setPassword("test1212");

    User addedUser = userDetailsServiceImpl.createAUser(newSignupUser);

    // Assert that the user was created successfully
    Assertions.assertEquals("test", addedUser.getFullName());
        Assertions.assertEquals("test", addedUser.getUsername());


    }

}