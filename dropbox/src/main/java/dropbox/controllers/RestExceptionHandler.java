package dropbox.controllers;


import dropbox.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;

// To handle and display the specific exceptions created instead of default 403.
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

     @ExceptionHandler(value = { UserAlreadyExistsException.class })
     public ResponseEntity<Object> handleUserAlreadyExists(Exception e, WebRequest
     request) {
     var error = new HashMap<String, Object> ();
     error.put("message", e.getMessage());
     return handleExceptionInternal(e, error, new HttpHeaders(),
     HttpStatusCode.valueOf(409), request);
     }

     @ExceptionHandler(value = { PasswordCannotBeNullException.class , UserNameCannotBeNullException.class })
     public ResponseEntity<Object> handleNullValue(Exception e, WebRequest
     request) {
     var error = new HashMap<String, Object>();
     error.put("message", e.getMessage());
     return handleExceptionInternal(e, error, new HttpHeaders(),
     HttpStatusCode.valueOf(400), request);
     }

     @ExceptionHandler(value = { FolderNameCannotBeNullException.class })
     public ResponseEntity<Object> handleFolderNameIsNull(Exception e, WebRequest
             request) {
          var error = new HashMap<String, Object>();
          error.put("message", e.getMessage());
          return handleExceptionInternal(e, error, new HttpHeaders(),
                  HttpStatusCode.valueOf(400), request);
     }

     @ExceptionHandler(value = { FolderNotFoundException.class })
     public ResponseEntity<Object> handleFolderNotFound(Exception e, WebRequest
             request) {
          var error = new HashMap<String, Object>();
          error.put("message", e.getMessage());
          return handleExceptionInternal(e, error, new HttpHeaders(),
                  HttpStatusCode.valueOf(400), request);
     }





}