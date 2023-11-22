package dropbox.repository;

import dropbox.models.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional // Because file uploads handles large data sets, this makes sure that everything goes through.
public interface FileRepository extends JpaRepository<File, Long> {


}
