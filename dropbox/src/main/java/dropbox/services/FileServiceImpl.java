package dropbox.services;

import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.models.Folder;
import dropbox.models.User;
import dropbox.repository.FileRepository;
import dropbox.repository.FolderRepository;
import dropbox.repository.UserRepository;
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


    // A method to upload any type of file by compressing it and then storing it in the db
    @Override
    public String uploadFile(MultipartFile data , Long folderId, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        int userId = user.getId();
        Optional<Folder> folderOptional = folderRepository.findUsersFolderById(folderId, userId);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            File file = new File();
            file.setFileName(data.getOriginalFilename());
            file.setFolder(folder);
            file.setFileType(data.getContentType());
            file.setFileByte(FileUtil.compressFile(data.getBytes()));

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
    public List<File> getAllFilesByFolderId(Long folderId) throws FolderNotFoundException {
        List<File> allFilesInAFolder = fileRepository.findAllByFolderId(folderId);
        if(allFilesInAFolder.isEmpty()){
            // No files found for the given folderId
            Folder folder = folderRepository.findById(folderId).orElse(null);
            if (folder == null){
                // Folder doesn't exist
                throw new FolderNotFoundException("Folder with id '"+ folderId + "' doesn't exists.");
            } else {
                // Folder exists but no files found
                return allFilesInAFolder;
            }
        }
        return allFilesInAFolder;
    }
    // To get a particular file by fileName and its parent folderId.
    @Override
    public Optional<File> retrieveFileByFileNameAndFolderId(String fileName, Long folderId) throws FileNotFoundException {
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
    public Optional<File> getUsersFileByFileId(Long fileId, Authentication authentication) throws FileNotFoundException {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        int userId = user.getId();
        Optional<File> myFile = this.fileRepository.findUsersFileByFileId(fileId, userId);
        if(!myFile.isPresent()){
            throw new FileNotFoundException("File with id '"+ fileId + "' not found.");
        }
        return myFile;

    }
    // To delete a file with its fileName and its parent folderId
    @Override
    public String removeFile(String fileName, Long folderId) throws Exception{
        Optional<File> file = retrieveFileByFileNameAndFolderId(fileName, folderId);
        if(!file.isPresent()){
            throw new Exception(String.format("File with name %s not found.", fileName));
        }
        this.fileRepository.delete(file.get());
        return "File '" + fileName + "' from folder id '" + folderId + "' deleted!";
    }

}
