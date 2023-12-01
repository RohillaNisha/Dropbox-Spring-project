package dropbox.controllers;
import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.models.User;
import dropbox.models.UserDetailsImpl;
import dropbox.payloads.request.LoginRequest;
import dropbox.payloads.request.SignupRequest;
import dropbox.payloads.response.JwtResponse;
import dropbox.repository.UserRepository;
import dropbox.security.JwtUtils;
import dropbox.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    // A signup endpoint for a new user creation
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser ( @Valid @RequestBody SignupRequest signupRequest) throws PasswordCannotBeNullException, UserNameCannotBeNullException, UserAlreadyExistsException {

       User addedUser = userDetailsServiceImpl.createAUser(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);


    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest)  {
       Authentication authentication = authenticationManager.authenticate(                 // try to take this logic in service. Create a new class for authlogic, and put createUser and this login inside that
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication); // can be removed from here and put it into SecurityConfig file,
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFullName(), roles)); // change this so that it doesn't have all the info even password

    }


}
