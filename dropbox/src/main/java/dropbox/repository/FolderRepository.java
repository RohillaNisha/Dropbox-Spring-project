package dropbox.repository;
import dropbox.models.Folder;
import dropbox.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {


    Optional<Folder> findByFolderName(String folderName);

    List<Folder> findByUserId(Integer userId);

    @Query(value = "SELECT * FROM folder WHERE id = :folderId AND user_id = :userId" , nativeQuery = true)
    Optional<Folder> findUsersFolderById(Long folderId, int userId);

    Optional<Folder> findByIdAndUserId(Long folderId, int userId);
}
