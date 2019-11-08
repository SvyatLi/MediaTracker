package ua.lsi.media_tracker.repository;

import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface MediaRepository {
    Media save(Media entity);

    Iterable<Media> findAll();

    boolean delete(Media entity);
}
