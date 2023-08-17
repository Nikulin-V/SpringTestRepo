package webserver.models.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import webserver.models.Image;

import java.util.Optional;

@Repository
public interface ImagesRepo extends CrudRepository<Image, String> {
    @NonNull
    Optional<Image> findById(@NonNull String s);

    @NonNull
    Optional<Image> findByName(@NonNull String name);
}
