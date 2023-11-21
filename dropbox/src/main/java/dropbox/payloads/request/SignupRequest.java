package dropbox.payloads.request;

import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
@Setter
public class SignupRequest {   // DTO for a signup request. Role is option as it is handled by the logic (default is 'ROLE_USER')

    private String fullName;
    private String username;
    private Set<String> role;
    private String password;


}
