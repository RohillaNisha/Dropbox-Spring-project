package dropbox.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

@Table(name= "users") // As user is a reserve word in postgresql, we change the name of the table to be users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name= "full_name", nullable = false)
    private String fullName;
    @Column(name="user_name", nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;

}
