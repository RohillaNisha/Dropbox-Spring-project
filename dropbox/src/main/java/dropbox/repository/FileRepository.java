package dropbox.repository;

import dropbox.models.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional // Because file uploads handles large data sets, this makes sure that everything goes through.
public interface FileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT fl.* FROM file fl LEFT JOIN folder fld ON fl.folder_id = fld.id WHERE fld.id = :folderId AND fld.user_id = :userId ", nativeQuery = true)
    List<File> findAllByFolderId(Long folderId, int userId);

    @Query(value = "SELECT * FROM file WHERE filename = :fileName AND folder_id = :folderId", nativeQuery = true)
    Optional<File> findByFileNameAndFolderId(String fileName, Long folderId);
    @Query(value = "SELECT fl.* FROM file fl LEFT JOIN folder fld ON fl.folder_id = fld.id WHERE fl.id = :fileId AND fld.user_id = :userId", nativeQuery = true)
    Optional<File> findUsersFileByFileId(Long fileId, int userId);

}
