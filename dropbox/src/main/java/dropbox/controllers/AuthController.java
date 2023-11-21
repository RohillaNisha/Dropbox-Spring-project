package dropbox.controllers;
import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.models.User;
import dropbox.payloads.request.SignupRequest;
import dropbox.payloads.response.MessageResponse;
import dropbox.repository.UserRepository;
import dropbox.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    UserRepository userRepository;

    // A signup endpoint for a new user creation
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser ( @Valid @RequestBody SignupRequest signupRequest) throws PasswordCannotBeNullException, UserNameCannotBeNullException, UserAlreadyExistsException {

       User addedUser = userDetailsServiceImpl.createAUser(signupRequest);

        return ResponseEntity.ok(addedUser);


    }
}
