package dropbox.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class UserNameCannotBeNullException extends RuntimeException{

    public UserNameCannotBeNullException(String message) {
        super(message);
    }
}
