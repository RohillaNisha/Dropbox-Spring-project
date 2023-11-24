package dropbox.controllers;


import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.models.User;
import dropbox.services.FileService;
import dropbox.services.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class FileController {

    @Autowired
    FileService fileService;

    // Upload a file to a specific folder mentioned i the params with its id
    @PostMapping("/upload/{folderId}")
    public ResponseEntity<String> uploadFile(@PathVariable Long folderId, @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {

        String uploadStatus = fileService.uploadFile(file, folderId, authentication);
        return ResponseEntity.ok(uploadStatus);

    }

    // An endpoint to get all the files in a particular folder of a user
    @GetMapping("/allFiles/{folderId}")
    public ResponseEntity<List<File>> getAllFilesInAFolder(@PathVariable Long folderId , Authentication authentication) throws FolderNotFoundException {
        List<File> filesInAFolder = fileService.getAllFilesByFolderId(folderId, authentication);
        return ResponseEntity.ok(filesInAFolder);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFileByFileId( @PathVariable Long fileId , Authentication authentication) throws FileNotFoundException {

        Optional<File> myFile = fileService.getUsersFileByFileId(fileId, authentication);
        if(!myFile.isPresent()){
        throw new FileNotFoundException("File not found");
        }
        File newFile = myFile.get();
        return ResponseEntity.ok(newFile);

    }

    // An endpoint to get file in a specific folder, by filename
    @GetMapping("/download/{folderId}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, @PathVariable Long folderId, Authentication authentication) throws FileNotFoundException {
        User user = fileService.getUserFromAuthentication(authentication);
        Optional<File> fileDetails = fileService.retrieveFileByFileNameAndFolderId(filename, folderId, user);
        if(!fileDetails.isPresent()){
            throw new FileNotFoundException("File with name '"+ filename + "' in folder with id '"+ folderId +"' not found.");
        }
        File foundFile = fileDetails.get();
        byte[] file = fileService.downloadFile(foundFile);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(foundFile.getFileType())).body(file);
    }

    // An endpoint to delete a file from a specific folder mentioned with folderId
    @DeleteMapping("/{folderId}/{filename}")
    public ResponseEntity<String> deleteFileByFileNameAndFolderId(@PathVariable Long folderId, @PathVariable String filename , Authentication authentication) throws Exception {
        User user = fileService.getUserFromAuthentication(authentication);
        Optional<File> fileToDelete = fileService.retrieveFileByFileNameAndFolderId(filename, folderId, user);
        if(!fileToDelete.isPresent()){
            throw new FileNotFoundException("File with name '"+ filename + "' in folder with id '"+ folderId +"' not found.");
        }
        String result = fileService.removeFile(filename, folderId,authentication);
        return ResponseEntity.ok(result);
    }



}









