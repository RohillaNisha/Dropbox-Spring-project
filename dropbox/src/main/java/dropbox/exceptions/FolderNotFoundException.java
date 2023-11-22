package dropbox.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FolderNotFoundException extends RuntimeException{
    public FolderNotFoundException(String message) {
        super(message);
    }
}
