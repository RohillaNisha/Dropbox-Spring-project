package dropbox.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FolderNameAlreadyExistsException extends RuntimeException{
    public FolderNameAlreadyExistsException(String message) {
        super(message);
    }
}
