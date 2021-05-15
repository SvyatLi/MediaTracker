package ua.lsi.media_tracker.dao;

import ua.lsi.media_tracker.model.Media;

import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public interface MediaContainer {

    Map<String, List<Media>> loadInformation();

    String saveMediaMap(Map<String, List<Media>> mediaMap);

    void removeMedia(Media media);

    void removeSection(String section);
}
