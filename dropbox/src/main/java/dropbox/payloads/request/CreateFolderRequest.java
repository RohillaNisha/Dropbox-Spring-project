package dropbox.payloads.request;


import dropbox.models.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateFolderRequest {

    @NotBlank
    private String folderName;

    private User folderOwner;

}
