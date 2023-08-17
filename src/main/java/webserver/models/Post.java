package webserver.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "posts")
public class Post implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String text;

    @Column(name = "created_at")
    @CreationTimestamp
    private String createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public Post() {
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void clearImages() {
        images.clear();
    }

    @SuppressWarnings("unused")
    public List<String> getImagesNames() {
        List<String> imagesNames = new ArrayList<>();
        for (Image image : images) {
            imagesNames.add(image.getName());
        }
        return imagesNames;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}