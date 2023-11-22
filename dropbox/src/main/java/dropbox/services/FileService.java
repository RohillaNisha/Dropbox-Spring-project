package dropbox.services;


import dropbox.models.File;
import dropbox.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {


    @Autowired
    private FileRepository fileRepository;

    public File persistFile(File file){
        return this.fileRepository.save(file);
    }


    public List<File> getAllFilesByFolderId(Long folderId) {
        List<File> allFilesInAFolder = fileRepository.findAllByFolderId(folderId);
        return allFilesInAFolder;
    }
}
