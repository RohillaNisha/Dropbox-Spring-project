package dropbox.controllers;


import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.services.FileService;
import dropbox.services.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> uploadFile(@PathVariable Long folderId, @RequestParam("file") MultipartFile file) throws IOException {

        String uploadStatus = fileService.uploadFile(file, folderId);
        return ResponseEntity.ok(uploadStatus);

    }

    // An endpoint to get all the files in a particular folder of a user
    @GetMapping("/allFiles/{folderId}")
    public ResponseEntity<List<File>> getAllFilesInAFolder(@PathVariable Long folderId) throws FolderNotFoundException {
        List<File> filesInAFolder = fileService.getAllFilesByFolderId(folderId);
        return ResponseEntity.ok(filesInAFolder);
    }

    @GetMapping("/{folderId}/{fileId}")
    public ResponseEntity<File> getFileByFileId(@PathVariable Long folderId, @PathVariable Long fileId) throws FileNotFoundException {
        Optional<File> myFile = fileService.getFileByFileId(fileId);
        if(!myFile.isPresent()){
        throw new FileNotFoundException("File not found");
        }
        File newFile = myFile.get();
        return ResponseEntity.ok(newFile);

    }

    // An endpoint to get file in a specific folder, by filename
    @GetMapping("/download/{folderId}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, @PathVariable Long folderId) throws FileNotFoundException {
        Optional<File> fileDetails = fileService.retrieveFileByFileNameAndFolderId(filename, folderId);
        if(!fileDetails.isPresent()){
            throw new FileNotFoundException("File with name '"+ filename + "' in folder with id '"+ folderId +"' not found.");
        }
        File foundFile = fileDetails.get();
        byte[] file = fileService.downloadFile(foundFile);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(foundFile.getFileType())).body(file);
    }

    // An endpoint to delete a file from a specific folder mentioned with folderId
    @DeleteMapping("/{folderId}/{filename}")
    public ResponseEntity<String> deleteFileByFileNameAndFolderId(@PathVariable Long folderId, @PathVariable String filename) throws Exception {
        Optional<File> fileToDelete = fileService.retrieveFileByFileNameAndFolderId(filename, folderId);
        if(!fileToDelete.isPresent()){
            throw new FileNotFoundException("File with name '"+ filename + "' in folder with id '"+ folderId +"' not found.");
        }
        String result = fileService.removeFile(filename, folderId);
        return ResponseEntity.ok(result);
    }



}









