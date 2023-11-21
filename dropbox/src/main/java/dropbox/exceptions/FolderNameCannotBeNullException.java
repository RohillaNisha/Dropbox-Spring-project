package dropbox.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class FolderNameCannotBeNullException extends RuntimeException {
    public FolderNameCannotBeNullException(String message) {
        super(message);
    }
}
