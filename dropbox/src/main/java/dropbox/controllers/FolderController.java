package dropbox.controllers;


import dropbox.models.Folder;
import dropbox.payloads.request.CreateFolderRequest;
import dropbox.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class FolderController {

    @Autowired
    FolderService folderService;

   // This endpoint uses authentication to take up the user's identity and set it as folderOwner and save in the db.

    @PostMapping("/new-folder")
    public ResponseEntity<Folder> createFolder(@RequestBody CreateFolderRequest createFolderRequest, Authentication authentication) {

        CreateFolderRequest readyFolderCreationRequest = folderService.setAFolderOwner(createFolderRequest, authentication);
        Folder newFolder = folderService.createFolder(readyFolderCreationRequest);

        return ResponseEntity.ok(newFolder);
    }

   // Endpoint to get all the folders of logged-in user using authentication
    @GetMapping("/my-folders")
    public ResponseEntity<List<Folder>> getMyAllFolders(Authentication authentication){
        List<Folder> userFolders = folderService.getFoldersOfCurrentUser(authentication);
        return ResponseEntity.ok(userFolders);
    }



}
