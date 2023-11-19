package dropbox.services;


import dropbox.repository.UserRepository;
import dropbox.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    UserRepository userRepository;

    public ResponseEntity<String> addNewUser(User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

}