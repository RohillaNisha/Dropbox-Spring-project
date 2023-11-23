package dropbox.services;


import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {


    @Autowired
    private FileRepository fileRepository;

    public File persistFile(File file){
        return this.fileRepository.save(file);
    }


    public List<File> getAllFilesByFolderId(Long folderId) throws FolderNotFoundException {
        List<File> allFilesInAFolder = fileRepository.findAllByFolderId(folderId);
        if(allFilesInAFolder == null || allFilesInAFolder.isEmpty()) {
            throw new FolderNotFoundException("Folder with id: '"+ folderId+ "' not found. ");
        }
        return allFilesInAFolder;
    }

    public File retrieveFileByFileNameAndFolderId(String fileName, Long folderId) throws FileNotFoundException {
        File fileRetrieved = this.fileRepository.findByFileNameAndFolderId(fileName, folderId);
        if(fileRetrieved == null){
            throw new FileNotFoundException("File with name '" + fileName + " in folder with id '"+ folderId + "' doesn't exist.");
        }
        else {
            return fileRetrieved;
        }
    }

    public Optional<File> getFileByFileId(Long fileId) throws FileNotFoundException {
        Optional<File> myFile = this.fileRepository.findById(fileId);
        if(!myFile.isPresent()){
            throw new FileNotFoundException("File with id '"+ fileId + "' not found.");
        }
        return myFile;

    }


}
