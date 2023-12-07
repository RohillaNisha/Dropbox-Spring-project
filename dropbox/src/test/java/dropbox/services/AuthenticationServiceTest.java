package dropbox.services;

import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.models.ERole;
import dropbox.models.Role;
import dropbox.models.User;
import dropbox.payloads.request.SignupRequest;
import dropbox.repository.RoleRepository;
import dropbox.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthenticationService authenticationService;


    @AfterEach
    @DisplayName("Deletes test user created during testing 'register a user' method")
    public void tearDown(){
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("Should pass when a new user is created")
    void testCreateAUser() throws UserAlreadyExistsException, UserNameCannotBeNullException, PasswordCannotBeNullException {
        Set<String> roles = new HashSet<>();
        roles.add("user");

        SignupRequest newSignupUser = new SignupRequest();
        newSignupUser.setFullName("test");
        newSignupUser.setUsername("test");
        newSignupUser.setPassword("test1212");
        newSignupUser.setRole(roles);



        when(bCryptPasswordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        boolean addedUser = authenticationService.createAUser(newSignupUser);

        // Assert that the user was created successfully

        Assertions.assertTrue(addedUser);
        verify(userRepository,times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(bCryptPasswordEncoder, times(1)).encode("test1212");

        // Mock userRepository to return a non-empty Optional to simulate an existing user
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authenticationService.createAUser(newSignupUser));


    }
}