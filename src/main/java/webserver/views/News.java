package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webserver.Templates;
import webserver.models.Image;
import webserver.models.Post;
import webserver.models.repos.ImagesRepo;
import webserver.models.repos.PostsRepo;
import webserver.models.repos.SettingsRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/news")
public class News {
    private final static String templateName = "news";
    @Autowired
    private SettingsRepo settingsRepo;
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private ImagesRepo imagesRepo;

    @GetMapping
    String news() {
        Iterable<Post> posts = postsRepo.findAll();

        String postTitleSize = "24px";
        if (settingsRepo.findById("Размер заголовка поста").isPresent())
            postTitleSize = settingsRepo.findById("Размер заголовка поста").get().getValue() + "px";
        String postTextSize = "14px";
        if (settingsRepo.findById("Размер текста поста").isPresent())
            postTextSize = settingsRepo.findById("Размер текста поста").get().getValue() + "px";

        Map<String, Object> context = Maps.newHashMap();
        context.put("posts", posts);
        context.put("postTitleSize", postTitleSize);
        context.put("postTextSize", postTextSize);
        return Templates.render(templateName, context);
    }

    @PostMapping
    String newsPost(@RequestBody HashMap<String, Object> json) {
        Post post = new Post();
        String title = (String) json.get("title");
        String text = (String) json.get("text");
        //noinspection unchecked
        List<String> imagesNames = (List<String>) json.get("images");

        if (title.isBlank())
            return "blank title";
        if (text.isBlank())
            return "blank text";

        post.setTitle(title);
        post.setText(text);
        postsRepo.save(post);

        for (String imageName : imagesNames) {
            Optional<Image> imageOptional = imagesRepo.findByName(imageName);
            if (imageOptional.isPresent()) {
                Image image = imageOptional.get();
                post.addImage(image);
                postsRepo.save(post);
                image.setPost(post);
                imagesRepo.save(image);
            }
        }
        return post.getId();
    }

    @PutMapping
    String newsEdit(@RequestBody HashMap<String, Object> json) {
        String id = (String) json.get("id");
        String title = (String) json.get("title");
        String text = (String) json.get("text");
        //noinspection unchecked
        List<String> imagesNames = (List<String>) json.get("images");
        Optional<Post> postOptional = postsRepo.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            if (title.isBlank())
                return "blank title";
            if (text.isBlank())
                return "blank text";

            post.setTitle(title);
            post.setText(text);
            postsRepo.save(post);

            if (!imagesNames.isEmpty())
                post.clearImages();
            for (String imageName : imagesNames) {
                Optional<Image> imageOptional = imagesRepo.findByName(imageName);
                if (imageOptional.isPresent()) {
                    Image image = imageOptional.get();
                    post.addImage(image);
                    postsRepo.save(post);
                    image.setPost(post);
                    imagesRepo.save(image);
                }
            }
            return post.getId();
        } else return "not found";
    }

    @DeleteMapping
    String newsDelete(@RequestBody HashMap<String, String> json) {
        String id = json.get("id");
        if (postsRepo.findById(id).isPresent()) {
            postsRepo.deleteById(id);
            return "success";
        } else return "id not found";
    }
}