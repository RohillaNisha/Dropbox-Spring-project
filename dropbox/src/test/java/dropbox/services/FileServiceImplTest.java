package dropbox.services;
import dropbox.models.Folder;
import dropbox.models.User;
import dropbox.repository.FolderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import java.io.IOException;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
class FileServiceImplTest {

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private Authentication authentication;

  /* @Test
    void testUploadFile() throws IOException {

       // Mock authentication
       User mockUser = new User();
       mockUser.setId(1);
       when(authentication.getName()).thenReturn("testUser");
       when(fileService.getUserFromAuthentication(any())).thenReturn(mockUser);

       // Mock folder repository behavior
       Folder mockFolder = new Folder();
       when(folderRepository.findUsersFolderById(anyLong(), anyInt())).thenReturn(Optional.of(mockFolder));


       // Mock fileUtil behavior
       when(fileUtil.c)

       // Call the uploadFile method to test
       String result = fileService.uploadFile()

    }

    @AfterEach
    public void tearDown(){
       SecurityContextHolder.clearContext();

    }
*/
}