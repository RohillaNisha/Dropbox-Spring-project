package dropbox.controllers;

import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.payloads.request.LoginRequest;
import dropbox.payloads.request.SignupRequest;
import dropbox.payloads.response.JwtResponse;
import dropbox.repository.UserRepository;
import dropbox.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;


    @Autowired
    UserRepository userRepository;

    // A signup endpoint for a new user creation
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws PasswordCannotBeNullException, UserNameCannotBeNullException, UserAlreadyExistsException {

        boolean result = authenticationService.createAUser(signupRequest);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }

    }
     // An endpoint for login of registered user
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse.getToken());
    }


}
