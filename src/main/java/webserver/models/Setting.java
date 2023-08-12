package webserver.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import webserver.SQLManager;

@Entity
@Table(name = "settings")
public class Setting {
    @Column
    private String name;
    @Column(name = "value", columnDefinition = "VARCHAR(128)")
    private String value;
    @Column
    private String description;

    @Id
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

    public static void create(String name, String value, String description) {
        Setting setting = new Setting();
        setting.setName(name);
        setting.setValue(value);
        setting.setDescription(description);
        create(setting);
    }

    public static void create(Setting setting) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(setting);
        } catch (PersistentObjectException e) {
            //noinspection deprecation
            session.save(setting);
        }
        transaction.commit();
        session.close();
    }

    public static Setting read(String name) {
        Session session = SQLManager.sessionFactory.openSession();
        Setting setting = session.get(Setting.class, name);
        session.close();
        return setting;
    }

    public static void update(Setting updatedSetting) {
        Session session = SQLManager.sessionFactory.openSession();
        Setting setting = read(updatedSetting.getName());
        setting.setName(updatedSetting.getName());
        setting.setValue(updatedSetting.getValue());
        setting.setDescription(updatedSetting.getDescription());
        Transaction transaction = session.beginTransaction();
        session.merge(setting);
        transaction.commit();
        session.close();
    }

    public static void delete(String name) {
        Session session = SQLManager.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(read(name));
        transaction.commit();
        session.close();
    }
}
