import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webserver.SQLManager;
import webserver.WebServer;
import webserver.models.Setting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SettingsTest {
    @BeforeAll
    static void setup() {
        WebServer.main(new String[0]);
        System.out.println("Server started");
    }

    @Test
    void createSetting() {
        String id = UUID.randomUUID().toString();
        String name = "Test name "  + Math.random();
        String value = String.valueOf(Math.random());
        String description = "Test description " + Math.random();

        Setting setting = new Setting();
        setting.setName(name);
        setting.setValue(value);
        setting.setDescription(description);
        Setting.create(setting);

        String query = String.format("""
                SELECT * FROM settings
                WHERE id = '%s' AND name = '%s' AND value = '%s' AND description = '%s'
                """,
                id, name, value, description);

        try (Connection connection = SQLManager.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            try {
                Assertions.assertNotNull(resultSet);
            } catch (AssertionError e) {
                Assertions.fail("Не создаётся Setting с указанными параметрами");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
