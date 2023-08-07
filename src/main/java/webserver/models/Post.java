package webserver.models;

import jakarta.persistence.*;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import webserver.SQLManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post implements Serializable {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(128)")
    private String id;
    private String title;
    private String text;

    @Column(name = "created_at", columnDefinition = "VARCHAR(32)")
    @CreationTimestamp
    private Date createdAt;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Post() {}

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

    @GeneratedValue(strategy = GenerationType.UUID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static void create(Post post) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(post);
        } catch (PersistentObjectException e) {
            //noinspection deprecation
            session.save(post);
        }
        transaction.commit();
        session.close();
    }

    public static Post read(String id) {
        Session session = SQLManager.sessionFactory.openSession();
        Post post = session.get(Post.class, id);
        session.close();
        return post;
    }

    public static void update(Post updatedPost) {
        Session session = SQLManager.sessionFactory.openSession();
        Post post = read(updatedPost.getId());
        post.setTitle(updatedPost.getTitle());
        post.setText(updatedPost.getText());
        post.setCreatedAt(new Date());
        Transaction transaction = session.beginTransaction();
        session.merge(post);
        transaction.commit();
        session.close();
    }

    public static void delete(String id) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(read(id));
        transaction.commit();
        session.close();
    }
}