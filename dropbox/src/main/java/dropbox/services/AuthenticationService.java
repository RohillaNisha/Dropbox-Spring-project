package dropbox.services;

import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.models.ERole;
import dropbox.models.Role;
import dropbox.models.User;
import dropbox.models.UserDetailsImpl;
import dropbox.payloads.request.LoginRequest;
import dropbox.payloads.request.SignupRequest;
import dropbox.payloads.response.JwtResponse;
import dropbox.repository.RoleRepository;
import dropbox.repository.UserRepository;
import dropbox.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    // Registration of a new user with fullName, username, role and password. Role should be an array of roles i.e ["admin","mod"] but is set to default value as ["user"]
    public boolean createAUser(SignupRequest signupRequest)
            throws UserAlreadyExistsException, UserNameCannotBeNullException, PasswordCannotBeNullException {
        if (signupRequest.getUsername() == null || signupRequest.getUsername().isBlank() || signupRequest.getUsername().isEmpty()) {
            throw new UserNameCannotBeNullException("Username cannot be null.");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().isBlank() || signupRequest.getPassword().isEmpty()) {
            throw new PasswordCannotBeNullException("Password cannot be null.");

        }
        var existing = userRepository.findByUsername(signupRequest.getUsername());
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException("The username " + signupRequest.getUsername() + " is not available to use.");
        }

        User userToBeAdded = new User(signupRequest.getFullName(), signupRequest.getUsername(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found. "));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        userToBeAdded.setRoles(roles);
       this.userRepository.save(userToBeAdded);
       return true;
    }


    public JwtResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid username or Password! "));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse loginResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFullName(), roles);
        return loginResponse ;


    }

}
