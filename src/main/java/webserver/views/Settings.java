package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webserver.Templates;
import webserver.models.Setting;
import webserver.models.repos.SettingsRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class Settings {
    private final static String templateName = "settings";
    @Autowired
    SettingsRepo settingsRepo;

    @GetMapping
    String settingsRead() {
        setDefaultSettings();
        Iterable<Setting> settings = settingsRepo.findAll();
        Map<String, Object> context = Maps.newHashMap();
        context.put("settings", settings);
        return Templates.render(templateName, context);
    }

    @PostMapping
    String settingsUpdate(@RequestParam HashMap<String, String> settings) {
        for (String settingName: settings.keySet()) {
            String settingValue = settings.get(settingName);
            Optional<Setting> settingOptional = settingsRepo.findById(settingName);
            if (settingOptional.isPresent()) {
                Setting setting = settingOptional.get();
                setting.setValue(settingValue);
                settingsRepo.save(setting);
            }
        }
        return settingsRead();
    }

//    @PostMapping
//    String settingsCreate(@RequestParam HashMap<String, String> settingFields) {
//        Setting setting = new Setting();
//        for (String settingField: settingFields.keySet()) {
//            String value = settingFields.get(settingField);
//            switch (settingField) {
//                case "name" -> setting.setName(value);
//                case "value" -> setting.setValue(value);
//                case "description" -> setting.setDescription(value);
//            }
//        }
//        settingsRepo.save(setting);
//        return settings();
//    }

    @DeleteMapping
    String settingsDelete(@RequestParam String settingName) {
        Optional<Setting> settingOptional = settingsRepo.findById(settingName);
        if (settingOptional.isEmpty())
            return null;
        settingsRepo.delete(settingOptional.get());
        return "success";
    }

    private void setDefaultSettings() {
        if (settingsRepo.findById("Размер заголовка поста").isEmpty())
            settingsRepo.create("Размер заголовка поста", "24", "Размер заголовка поста в px");
        if (settingsRepo.findById("Размер текста поста").isEmpty())
            settingsRepo.create("Размер текста поста", "14", "Размер текста поста в px");
    }
}
