package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webserver.SQLManager;
import webserver.Templates;
import webserver.models.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class News {
    private final static String templateName = "news";

    @GetMapping
    String news() {
        ArrayList<Post> posts = new ArrayList<>();

        String query = "SELECT * FROM posts";

        try (Connection connection = SQLManager.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int i = 0;
                Post post = new Post();
                post.setId(rs.getString(++i));
                post.setTitle(rs.getString(++i));
                post.setText(rs.getString(++i));
                post.setCreatedAt(rs.getDate(++i));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> context = Maps.newHashMap();
        context.put("posts", posts);
        return Templates.render(templateName, context);
    }
}