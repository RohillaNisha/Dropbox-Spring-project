package dropbox.controllers;


import dropbox.models.Folder;
import dropbox.payloads.request.CreateFolderRequest;
import dropbox.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class FolderController {

    @Autowired
    FolderService folderService;

    // This endpoint uses token to take up the user's identity and set id as folderOwnerId and save in the db.

    @PostMapping("/new-folder")
    public ResponseEntity<Folder> createFolder(@RequestHeader("Authorization") String token, @RequestBody CreateFolderRequest createFolderRequest) {

        CreateFolderRequest readyFolderCreationRequest = folderService.setAFolderOwner(createFolderRequest, token);
        Folder newFolder = folderService.createFolder(readyFolderCreationRequest);

        return ResponseEntity.ok(newFolder);
    }

    // Endpoint to get all the folders of logged-in user using authentication
    @GetMapping("/my-folders")
    public ResponseEntity<List<Folder>> getMyAllFolders(@RequestHeader("Authorization") String token) {
        List<Folder> userFolders = folderService.getFoldersOfCurrentUser(token);
        return ResponseEntity.ok(userFolders);
    }


}
