package ua.lsi.media_tracker.repository;

import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface MediaRepository{
    boolean save(Media entity);

    boolean update(Media entity);

    boolean save(Iterable<Media> entities);

    Iterable<Media> findAll();

    boolean delete(Media entity);
}
