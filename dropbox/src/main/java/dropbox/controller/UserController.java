package dropbox.controller;


import dropbox.model.User;
import dropbox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("addUser")
    public ResponseEntity<String> addNewUser (@RequestBody User user){
        return userService.addNewUser(user);
    }
}
