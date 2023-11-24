package dropbox.services;


import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
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

String uploadFile(MultipartFile data, Long folderId, Authentication authentication) throws IOException;
File persistFile(File file);
List<File> getAllFilesByFolderId(Long folderId, Authentication authentication) throws FolderNotFoundException;
byte[] downloadFile(File retrievedFile);
Optional<File> retrieveFileByFileNameAndFolderId(String fileName, Long folderId) throws FileNotFoundException;
Optional<File> getUsersFileByFileId(Long fileId, Authentication authentication) throws FileNotFoundException;
String removeFile(String fileName, Long folderId) throws Exception;

}
