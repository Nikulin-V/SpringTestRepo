package webserver.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "images")
public class Image {
    @Column(name = "id", columnDefinition = "VARCHAR(128)")
    private UUID id;
    private String name;
    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;
    @Column(name = "post_id", columnDefinition = "VARCHAR(128)")
    private UUID postId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }
}
