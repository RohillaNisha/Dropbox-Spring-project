package dropbox.controllers;
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


    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser ( @Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already taken. "));
        }

       User addedUser = userDetailsServiceImpl.createAUser(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));


    }
}
