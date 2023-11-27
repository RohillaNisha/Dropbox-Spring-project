package dropbox.services;

import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.payloads.request.SignupRequest;
import dropbox.repository.RoleRepository;
import dropbox.repository.UserRepository;
import dropbox.models.User;
import dropbox.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class UserDetailsServiceImplTest {

@Autowired
UserRepository userRepository;
@Autowired
BCryptPasswordEncoder bCryptPasswordEncoder;

@Autowired
private RoleRepository roleRepository;

@Autowired
private UserDetailsServiceImpl userDetailsServiceImpl;


    @AfterEach
    @DisplayName("Deletes test user created during testing 'register a user' method")
    public void tearDown(){
        userRepository.deleteAll();
    }


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