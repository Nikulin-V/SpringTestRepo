package webserver;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import webserver.models.Setting;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

@Component
public class SQLManager {
    public static final SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    public static final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    SQLManager() {
        createSettingsTable();
        createPostsTable();
        createImagesTable();
    }

    @Bean
    public static DataSource dataSource() {
        String driverClassName = "org.sqlite.JDBC";
        String dataSourceUrl = "jdbc:sqlite:database.db";

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dataSourceUrl);
        return dataSourceBuilder.build();
    }

    public void createSettingsTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS settings(
                name TEXT(128) NOT NULL PRIMARY KEY,
                value VARCHAR(128) NOT NULL,
                description TEXT(128)
            )
            """);
        if (Setting.read("Размер заголовка поста") == null)
            Setting.create("Размер заголовка поста", "24", "Размер заголовка поста в px");
        if (Setting.read("Размер текста поста") == null)
            Setting.create("Размер текста поста", "14", "Размер текста поста в px");
    }

    public void createPostsTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS posts(
                id VARCHAR(128) NOT NULL PRIMARY KEY,
                title TEXT(128) NOT NULL,
                text TEXT(4096) NOT NULL,
                created_at VARCHAR(32)
            )
            """);
    }

    public void createImagesTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS images(
                id VARCHAR(128) NOT NULL PRIMARY KEY,
                name TEXT(128) NOT NULL,
                image BLOB(16777216) NOT NULL,
                post_id VARCHAR(128) NOT NULL,
                FOREIGN KEY (post_id) REFERENCES posts(id)
            )
            """);
    }
}
