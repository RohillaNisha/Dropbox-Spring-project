package dropbox.services;


import dropbox.exceptions.PasswordCannotBeNullException;
import dropbox.exceptions.UserAlreadyExistsException;
import dropbox.exceptions.UserNameCannotBeNullException;
import dropbox.models.ERole;
import dropbox.models.Role;
import dropbox.models.UserDetailsImpl;
import dropbox.payloads.request.SignupRequest;
import dropbox.repository.RoleRepository;
import dropbox.repository.UserRepository;
import dropbox.models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDetailsServiceImpl(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public User createAUser(SignupRequest signupRequest)
            throws UserAlreadyExistsException, UserNameCannotBeNullException, PasswordCannotBeNullException {
        if(signupRequest.getUsername() == null  || signupRequest.getUsername().isBlank()|| signupRequest.getUsername().isEmpty()){
            throw new UserNameCannotBeNullException("Username cannot be null.");
        }
        if(signupRequest.getPassword() == null  || signupRequest.getPassword().isBlank()|| signupRequest.getPassword().isEmpty()){
            throw new PasswordCannotBeNullException("Password cannot be null.");

        }
        var existing = this.userRepository.findByUsername(signupRequest.getUsername());
        if ( existing.isPresent()){
            throw new UserAlreadyExistsException("The username "+ signupRequest.getUsername()+ " is not available to use.");
        }

        User userToBeAdded = new User( signupRequest.getFullName(),signupRequest.getUsername(), bCryptPasswordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
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
        return this.userRepository.save(userToBeAdded);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the username: "+ username));
        return UserDetailsImpl.build(user);

    }

}