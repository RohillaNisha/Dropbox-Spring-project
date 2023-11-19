package dropbox.payloads.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {


    private String fullName;
    private String username;
    private Set<String> role;
    private String password;


}
