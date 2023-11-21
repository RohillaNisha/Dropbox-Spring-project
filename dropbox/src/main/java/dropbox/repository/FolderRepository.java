package dropbox.repository;
import dropbox.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {


    Optional<Folder> findByFolderName(String folderName);
}
