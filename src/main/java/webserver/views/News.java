package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;
import webserver.SQLManager;
import webserver.Templates;
import webserver.models.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
                post.setCreatedAt(rs.getString(++i));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> context = Maps.newHashMap();
        context.put("posts", posts);
        return Templates.render(templateName, context);
    }

    @PostMapping
    String newsPost(@RequestBody HashMap<String, String> json) {
        String title = json.get("title");
        String text = json.get("text");
        return Post.create(title, text);
    }

    @PutMapping
    String newsPut(@RequestBody HashMap<String, String> json) {
        String id = json.get("id");
        String title = json.get("title");
        String text = json.get("text");

        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setText(text);
        Post.update(post);
        return "success";
    }

    @DeleteMapping
    String newsDelete(@RequestBody HashMap<String, String> json) {
        String id = json.get("id");

        if (Post.read(id) != null) {
            Post.delete(id);
            return "success";
        } else return "id not found";
    }
}