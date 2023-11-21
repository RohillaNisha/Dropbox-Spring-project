package dropbox.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String folderName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder(String folderName, User user) {
    }
}
