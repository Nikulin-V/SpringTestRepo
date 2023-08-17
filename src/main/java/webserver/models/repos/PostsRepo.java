package webserver.models.repos;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import webserver.models.Post;


@Repository
public interface PostsRepo extends CrudRepository<Post, String> {
    default String create(String title, String text) {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        save(post);
        return post.getId();
    }

    Iterable<Post> findAll(Sort sort);
}
