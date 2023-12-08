package dropbox.services;


import dropbox.exceptions.FolderNameAlreadyExistsException;
import dropbox.exceptions.FolderNameCannotBeNullException;
import dropbox.models.Folder;
import dropbox.models.User;
import dropbox.payloads.request.CreateFolderRequest;
import dropbox.repository.FolderRepository;
import dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    FolderRepository folderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileServiceImpl fileService;

    // A function to add the user ( logged in) as the folderOwner
    public CreateFolderRequest setAFolderOwner(CreateFolderRequest createFolderRequest, String token){
/*
       String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
*/
        User user = fileService.getUserFromToken(token);
        createFolderRequest.setFolderOwnerId(user.getId());
        return createFolderRequest;

    }
     // to save the folder created in the database
    public Folder createFolder(CreateFolderRequest createFolderRequest) throws FolderNameCannotBeNullException , FolderNameAlreadyExistsException {
         if(createFolderRequest.getFolderName() == null || createFolderRequest.getFolderName().isBlank() || createFolderRequest.getFolderName().isEmpty() ){
             throw new FolderNameCannotBeNullException("Folder must be named.");
         }

         var existing = this.folderRepository.findByFolderName(createFolderRequest.getFolderName());

         if(existing.isPresent()){
             throw new FolderNameAlreadyExistsException( " A folder with name: '" + createFolderRequest.getFolderName() + "' already exist.");
         }
         Folder folderToBeCreated = new Folder();
         folderToBeCreated.setFolderName( createFolderRequest.getFolderName());
         folderToBeCreated.setUserId(createFolderRequest.getFolderOwnerId());
         return this.folderRepository.save(folderToBeCreated);
    }

    // getting user object from the authentication
    public List<Folder> getFoldersOfCurrentUser(String token){
        User user = fileService.getUserFromToken(token);

      /*  String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found! "));
*/
         List<Folder> userFolders = folderRepository.findByUserId(user.getId());
         return userFolders;

    }



}
