package dropbox.repository;

import dropbox.models.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional // Because file uploads handles large data sets, this makes sure that everything goes through.
public interface FileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT * FROM file WHERE folder_id = :folderId ", nativeQuery = true)
    List<File> findAllByFolderId(Long folderId);
}
