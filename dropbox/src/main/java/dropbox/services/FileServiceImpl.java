package dropbox.services;

import dropbox.models.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


@Service
public class FileServiceImpl {
    @Autowired
    FileService fileService;

    public String uploadMultipartFile(MultipartFile data){
        File file = new File();
        file.setFileName(data.getOriginalFilename());
        file.setFileType(data.getContentType());
        try{
            file.setFileByte(FileUtil.compressFile(data.getBytes()));
        } catch (IOException e){
            e.printStackTrace();
        }

        File newFile = this.fileService.persistFile(file);
        if(newFile != null) {
            return String.format("File %s uploaded successfully! ", data.getOriginalFilename());
        }

        return String.format("File %s upload failed. ", data.getOriginalFilename());
    }

    public File retrieveFile(String fileName){
        return fileService.retrieveFileByFileName(fileName);
    }

    public byte[] downloadMultipartFile(String fileName) {
        return FileUtil.deCompressFile(fileService.retrieveFileByFileName(fileName).getFileByte());
    }


}
