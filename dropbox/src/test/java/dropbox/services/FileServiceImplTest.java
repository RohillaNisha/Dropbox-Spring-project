package dropbox.services;
import dropbox.models.File;
import dropbox.models.Folder;
import dropbox.models.User;
import dropbox.repository.FileRepository;
import dropbox.repository.FolderRepository;
import dropbox.repository.UserRepository;
import dropbox.security.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private FileUtil fileUtil;

    @Mock
    private UserRepository userRepository;


    @Test
    void testUploadFile() throws IOException {


        Long folderId = 1L;

        // Mock user and token
        User mockUser = createMockUser();
        String token = "validToken";

        // Mock folder repository behavior
        Folder mockFolder = new Folder();
        when(folderRepository.findUsersFolderById(anyLong(), anyInt())).thenReturn(Optional.of(mockFolder));


        // Mock getUserFromToken
        when(jwtUtils.getUsernameFromJwtToken(any())).thenReturn("test");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));


        // Mock persistFile behavior
        when(fileService.persistFile(any())).thenReturn(mock(File.class));

        // Create a mock MultipartFile
        MultipartFile mockMultipartFile = new MockMultipartFile("data", "filename", "text/plain", "File content".getBytes(StandardCharsets.UTF_8));


        // Call the uploadFile method to test
        String result = fileService.uploadFile(mockMultipartFile, 1L, token);

        // Verify interactions and assertions
        Assertions.assertEquals("File filename uploaded successfully! ", result);

    }


    private User createMockUser() {
        User mockUser = Mockito.mock(User.class);
        mockUser.setId(1);
        mockUser.setFullName("testUser");
        mockUser.setUsername("test");
        return mockUser;
    }



}