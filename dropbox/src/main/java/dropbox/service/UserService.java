package dropbox.service;


import dropbox.dao.UserDao;
import dropbox.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    UserDao userDao;

    public ResponseEntity<String> addNewUser(User user) {
        try {
            userDao.save(user);
            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

}