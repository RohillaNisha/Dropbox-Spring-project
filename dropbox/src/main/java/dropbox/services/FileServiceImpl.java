package dropbox.services;

import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.models.Folder;
import dropbox.models.User;
import dropbox.repository.FileRepository;
import dropbox.repository.FolderRepository;
import dropbox.repository.UserRepository;
import dropbox.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class FileServiceImpl implements FileService{

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FileRepository fileRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public User getUserFromToken(String token) {
        String cleanToken = jwtUtils.getCleanToken(token);
        String username = jwtUtils.getUsernameFromJwtToken (cleanToken);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            return newUser;

        }
        else{
            return null;
        }
    }

    // A method to upload any type of file by compressing it and then storing it in the db
    @Override
    public String uploadFile(MultipartFile data , Long folderId, String token ) throws IOException {
       User user = getUserFromToken(token);
        int userId = user.getId();
        Optional<Folder> folderOptional = folderRepository.findUsersFolderById(folderId, userId);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            File file = new File();
            file.setFileName(data.getOriginalFilename());
            file.setFolder(folder);
            file.setFileType(data.getContentType());
            file.setFileByte(data.getBytes());
           try {
                file.setFileByte(FileUtil.compressFile(data.getBytes()));
            } catch (IllegalArgumentException e){
                return String.format("File %s upload failed.",data.getOriginalFilename() );
            }
            File newFile = persistFile(file);
            if (newFile != null) {
                return String.format("File %s uploaded successfully! ", data.getOriginalFilename());
            }

            return String.format("File %s upload failed. ", data.getOriginalFilename());
        } else {
            throw new FolderNotFoundException("Folder with ID "+ folderId + " not found.");
        }
    }

     // To download the file from the database by first decompressing it and then downloading it.
     @Override
     public byte[] downloadFile(File retrievedFile) {
        return FileUtil.deCompressFile(retrievedFile.getFileByte());
    }

    // Method ensures that the file is saved in the db.
    @Override
    public File persistFile(File file){
        return this.fileRepository.save(file);
    }

    // To get all the files in a particular folder ( By folderId)
    @Override
    public List<File> getAllFilesByFolderId(Long folderId,  String token) throws FolderNotFoundException {
        User user = getUserFromToken(token);
        int userId = user.getId();
        // checks if the folder exists and belongs to the user
        Optional<Folder> folderOptional = folderRepository.findByIdAndUserId(folderId, userId);
        if (folderOptional.isEmpty()) {
            throw new FolderNotFoundException("Folder with id '" + folderId + "' doesn't exist or doesn't belong to the user.");
        }
        return fileRepository.findAllByFolderId(folderId, userId);
    }


    // To get a particular file by fileName and its parent folderId.
    @Override
    public Optional<File> retrieveFileByFileNameAndFolderId(String fileName, Long folderId, User user) throws FileNotFoundException {
        Optional<Folder> folderOptional = folderRepository.findByIdAndUserId(folderId, user.getId());
        if (folderOptional.isEmpty()) {
            throw new FolderNotFoundException("Folder with id '" + folderId + "' doesn't exist or doesn't belong to the user.");
        }
        Optional<File> fileRetrieved = this.fileRepository.findByFileNameAndFolderId(fileName, folderId);
        if(!fileRetrieved.isPresent()){
            throw new FileNotFoundException("File with name '" + fileName + " in folder with id '"+ folderId + "' doesn't exist.");
        }
        else {
            return fileRetrieved;
        }
    }


     // To get a particular file with its unique fileId;
    @Override
    public Optional<File> getUsersFileByFileId(Long fileId, String token) throws FileNotFoundException {
        User user = getUserFromToken(token);
        int userId = user.getId();
        Optional<File> myFile = this.fileRepository.findUsersFileByFileId(fileId, userId);
        if(!myFile.isPresent()){
            throw new FileNotFoundException("File with id '"+ fileId + "' not found.");
        }
        return myFile;

    }
    // To delete a file with its fileName and its parent folderId
    @Override
    public String removeFile(String fileName, Long folderId,String token) throws Exception{
        User user = getUserFromToken(token);
        Optional<File> file = retrieveFileByFileNameAndFolderId(fileName, folderId,user);
        if(!file.isPresent()){
            throw new Exception(String.format("File with name %s not found.", fileName));
        }
        this.fileRepository.delete(file.get());
        return "File '" + fileName + "' from folder id '" + folderId + "' deleted!";
    }

}
