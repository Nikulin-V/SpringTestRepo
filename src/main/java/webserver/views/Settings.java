package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;
import webserver.SQLManager;
import webserver.Templates;
import webserver.models.Setting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/settings")
public class Settings {
    private final static String templateName = "settings";

    @GetMapping
    String settings() {
        ArrayList<Setting> settings = new ArrayList<>();
        
        String query = "SELECT * FROM settings";

        try (Connection connection = SQLManager.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int i = 0;
                Setting setting = new Setting();
                setting.setName(rs.getString(++i));
                setting.setValue(rs.getString(++i));
                setting.setDescription(rs.getString(++i));
                settings.add(setting);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> context = Maps.newHashMap();
        context.put("settings", settings);
        return Templates.render(templateName, context);
    }

    @PostMapping
    String settingsUpdate(@RequestParam HashMap<String, String> settings) {
        for (String settingName: settings.keySet()) {
            String settingValue = settings.get(settingName);
            Setting setting = Setting.read(settingName);
            setting.setValue(settingValue);
            Setting.update(setting);
        }
        return settings();
    }

    @PutMapping
    String settingsCreate(@RequestParam HashMap<String, String> settingFields) {
        Setting setting = new Setting();
        for (String settingField: settingFields.keySet()) {
            String value = settingFields.get(settingField);
            switch (settingField) {
                case "name" -> setting.setName(value);
                case "value" -> setting.setValue(value);
                case "description" -> setting.setDescription(value);
            }
        }
        Setting.create(setting);
        return settings();
    }

    @DeleteMapping
    String settingsDelete(@RequestParam String settingName) {
        Setting.delete(settingName);
        return settings();
    }
}
