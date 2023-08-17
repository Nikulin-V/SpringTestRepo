package webserver.models.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import webserver.models.Setting;

@Repository
public interface SettingsRepo extends CrudRepository<Setting, String> {
    default void create(String name, String value, String description) {
        Setting setting = new Setting();
        setting.setName(name);
        setting.setValue(value);
        setting.setDescription(description);
        save(setting);
    }
}
