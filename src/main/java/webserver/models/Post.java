package webserver.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {
    @Column(name = "id", columnDefinition = "VARCHAR(128)")
    private UUID id;
    private String title;
    private String text;
    @Column(name = "created_at", columnDefinition = "VARCHAR(32)")
    private Date createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}