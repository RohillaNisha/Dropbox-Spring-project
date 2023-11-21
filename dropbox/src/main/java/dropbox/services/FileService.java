package dropbox.services;


import dropbox.models.File;
import dropbox.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {


    @Autowired
    private FileRepository fileRepository;

    public File persistFile(File file){
        return this.fileRepository.save(file);
    }
    public File retrieveFileByFileName(String fileName){
        return this.fileRepository.findByFileName(fileName);
    }

    public void removeFile(String fileName) throws Exception{
        File file = retrieveFileByFileName(fileName);
        if(file == null){
            throw new Exception(String.format("File with name %s not found.", fileName));
        }
        this.fileRepository.delete(file);
    }

}
