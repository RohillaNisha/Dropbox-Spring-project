package dropbox.payloads.request;

import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
@Setter
public class SignupRequest {


    private String fullName;
    private String username;
    private Set<String> role;
    private String password;


}
