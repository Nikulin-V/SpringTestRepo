package webserver.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "images")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
