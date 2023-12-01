package dropbox.services;


import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.models.User;
import dropbox.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {

User getUserFromToken(String token);
String uploadFile(MultipartFile data, Long folderId, String token) throws IOException;
File persistFile(File file);
List<File> getAllFilesByFolderId(Long folderId,  String token) throws FolderNotFoundException;
byte[] downloadFile(File retrievedFile);
Optional<File> retrieveFileByFileNameAndFolderId(String fileName, Long folderId, User user) throws FileNotFoundException;
Optional<File> getUsersFileByFileId(Long fileId,  String token) throws FileNotFoundException;
String removeFile(String fileName, Long folderId, String token) throws Exception;

}
