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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        reset(userRepository, bCryptPasswordEncoder, roleRepository);
    }


    @Test
    @DisplayName("Should pass when a new user is created")
    void testCreateAUser() throws UserAlreadyExistsException, UserNameCannotBeNullException, PasswordCannotBeNullException {

        // Mock the behavior of userRepository.findByUsername to return empty optional
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Mock the behavior of roleRepo.findByName to return a user role
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));

        // Mock the behavior of bCryptPasswordEncoder.encode to return a dummy password
        when(bCryptPasswordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        // Create a new Signup Request
        SignupRequest newSignupUser = new SignupRequest();
        newSignupUser.setFullName("test");
        newSignupUser.setUsername("test");
        newSignupUser.setPassword("test1212");

        // Calling the method to be tested
        boolean addedUser = authenticationService.createAUser(newSignupUser);

        // Assert that the user was created successfully
        Assertions.assertTrue(addedUser);
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(bCryptPasswordEncoder, times(1)).encode("test1212");

        // Mock userRepository to return a non-empty Optional to simulate an existing user
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authenticationService.createAUser(newSignupUser));


    }
}