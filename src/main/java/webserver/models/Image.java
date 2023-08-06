package webserver.models;

import jakarta.persistence.*;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import webserver.SQLManager;

@Entity
@Table(name = "images")
public class Image {
    @Column(name = "id", columnDefinition = "VARCHAR(128)")
    private String id;
    private String name;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;

    @ManyToOne
    private Post post;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public static void create(Image image) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(image);
        } catch (PersistentObjectException e) {
            //noinspection deprecation
            session.save(image);
        }
        transaction.commit();
        session.close();
    }

    public static Image read(String id) {
        Session session = SQLManager.sessionFactory.openSession();
        Image image = session.get(Image.class, id);
        session.close();
        return image;
    }

    public static void update(Image updatedImage) {
        Session session = SQLManager.sessionFactory.openSession();
        Image image = read(updatedImage.getId());
        image.setId(updatedImage.getId());
        image.setName(updatedImage.getName());
        image.setImage(updatedImage.getImage());
        image.setPost(updatedImage.getPost());
        Transaction transaction = session.beginTransaction();
        session.merge(image);
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
