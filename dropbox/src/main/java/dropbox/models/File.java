package dropbox.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"fileName", "folder_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "filetype")
    private String fileType;

    @Column(name = "filebyte", length= 5000)
    private byte[] fileByte;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;




}
