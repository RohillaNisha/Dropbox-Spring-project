package dropbox.controllers;


import dropbox.models.File;
import dropbox.services.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class FileController {

 @Autowired
    FileServiceImpl fileServiceImpl;

 @PostMapping("/upload/{folderId}")
    public ResponseEntity<String> uploadFile(@PathVariable Long folderId, @RequestParam("file")MultipartFile file) throws IOException {

         String uploadStatus = fileServiceImpl.uploadFile(file, folderId);
         return ResponseEntity.ok(uploadStatus);




 }

}
