package dropbox.controllers;


import dropbox.models.File;
import dropbox.services.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FileController {

 @Autowired
    FileServiceImpl fileServiceImpl;

   // Upload a file to a specific folder mentioned i the params with its id
 @PostMapping("/upload/{folderId}")
     public ResponseEntity<String> uploadFile(@PathVariable Long folderId, @RequestParam("file")MultipartFile file) throws IOException {

     String uploadStatus = fileServiceImpl.uploadFile(file, folderId);
     return ResponseEntity.ok(uploadStatus);

 }





}
