package webserver.models;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import webserver.SQLManager;

import java.util.UUID;

@Entity
@Table(name = "settings")
public class Setting {
    @Column(name = "id", columnDefinition = "VARCHAR(128)")
    private UUID id;
    @Column
    private String name;
    @Column(name = "value", columnDefinition = "VARCHAR(128)")
    private String value;
    @Column
    private String description;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void create(Setting setting) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(setting);
        transaction.commit();
        session.close();
    }

    public static Setting read(UUID id) {
        Session session = SQLManager.sessionFactory.openSession();
        Setting setting = session.get(Setting.class, id);
        session.close();
        return setting;
    }

    public static void update(Setting updatedSetting) {
        Session session = SQLManager.sessionFactory.openSession();
        Setting setting = read(updatedSetting.getId());
        setting.setName(updatedSetting.getName());
        setting.setValue(updatedSetting.getValue());
        setting.setDescription(updatedSetting.getDescription());
        Transaction transaction = session.beginTransaction();
        session.merge(setting);
        transaction.commit();
        session.close();
    }

    public static void delete(UUID id) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(read(id));
        transaction.commit();
        session.close();
    }
}
