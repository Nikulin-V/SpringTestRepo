import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webserver.SQLManager;
import webserver.WebServer;
import webserver.models.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PostsTest {
    @BeforeAll
    static void setup() {
        WebServer.main(new String[0]);
        System.out.println("Server started");
    }

    @Test
    void createPost() {
        String id = UUID.randomUUID().toString();
        String title = "Test title "  + Tests.randomInt();
        String text = "Test text" + Tests.randomInt();

        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setText(text);
        Post.create(post);

        String query = String.format("""
                SELECT * FROM posts
                WHERE id = '%s' AND title = '%s' AND text = '%s'
                """,
                id, title, text);

        try (Connection connection = SQLManager.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            try {
                Assertions.assertNotNull(resultSet);
            } catch (AssertionError e) {
                Assertions.fail("Не создаётся Post с указанными параметрами");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
