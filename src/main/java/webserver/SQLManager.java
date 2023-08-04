package webserver;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SQLManager {
    public static final SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    public static final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

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
                id VARCHAR(128) PRIMARY KEY,
                name TEXT(128),
                value VARCHAR(128),
                description TEXT(128)
            )
            """);
    }

    public void createPostsTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS posts(
                id VARCHAR(128) PRIMARY KEY,
                title TEXT(128),
                text TEXT(4096),
                created_at VARCHAR(32)
            )
            """);
    }

    public void createImagesTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS images(
                id VARCHAR(128) PRIMARY KEY,
                name TEXT(128),
                image BLOB(16777216),
                post_id VARCHAR(128),
                FOREIGN KEY (post_id) REFERENCES posts(id)
            )
            """);
    }
}
