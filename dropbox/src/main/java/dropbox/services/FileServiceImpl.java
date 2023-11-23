package dropbox.services;

import dropbox.exceptions.FolderNotFoundException;
import dropbox.models.File;
import dropbox.models.Folder;
import dropbox.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class FileServiceImpl {
    @Autowired
    FileService fileService;

    @Autowired
    FolderRepository folderRepository;

    public String uploadFile(MultipartFile data , Long folderId) throws IOException {

        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            File file = new File();
            file.setFileName(data.getOriginalFilename());
            file.setFolder(folder);
            file.setFileType(data.getContentType());
            System.out.println("Get bytes of the file before compress: " + data.getBytes());
           // byte[] fileSize = FileUtil.compressFile(data.getBytes());
            //System.out.println("size of te file is " + fileSize);
          //  file.setFileByte(FileUtil.compressFile(data.getBytes()));
            file.setFileByte(data.getBytes());


            File newFile = this.fileService.persistFile(file);
            if (newFile != null) {
                return String.format("File %s uploaded successfully! ", data.getOriginalFilename());
            }

            return String.format("File %s upload failed. ", data.getOriginalFilename());
        } else {
            throw new FolderNotFoundException("Folder with ID "+ folderId + " not found.");
        }
    }

    public List<File> getAllFilesByFolderId(Long folderId) throws FolderNotFoundException {
        List<File> filesInAFolder = fileService.getAllFilesByFolderId(folderId);
        return filesInAFolder;
    }

    public byte[] downloadFile(File retrievedFile) {
        return retrievedFile.getFileByte();
    }

    public File retrieveFile(String fileName, Long folderId) throws FileNotFoundException {
        File retrievedFile = fileService.retrieveFileByFileNameAndFolderId(fileName, folderId);
        System.out.println("retrieved File " + retrievedFile);
        return retrievedFile;
    }


}
