package webserver.models;

import jakarta.persistence.*;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import webserver.SQLManager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = SQLManager.dateFormatter.format(createdAt);
    }

    public static String create(Post post) {
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
        return post.getId();
    }

    public static String create(String title, String text) {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        return Post.create(post);
    }

    public static Post read(String id) {
        Post post = null;

        String query = String.format("SELECT * FROM posts WHERE id = '%s'", id);

        try (Connection connection = SQLManager.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int i = 0;
                post = new Post();
                post.setId(rs.getString(++i));
                post.setTitle(rs.getString(++i));
                post.setText(rs.getString(++i));
                post.setCreatedAt(rs.getString(++i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    public static void update(Post updatedPost) {
        Post post = read(updatedPost.getId());
        post.setTitle(updatedPost.getTitle());
        post.setText(updatedPost.getText());
        post.setCreatedAt(new Date());

        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //noinspection deprecation
        session.update(post);
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