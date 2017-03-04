package ua.lsi.media_tracker.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface MediaRepository extends CrudRepository<Media, Long> {
}
