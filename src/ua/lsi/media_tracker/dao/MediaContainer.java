package ua.lsi.media_tracker.dao;

import ua.lsi.media_tracker.model.Media;

import java.util.List;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public interface MediaContainer {

    void init();

    List<Media> getAll();

    void saveAll();
}
