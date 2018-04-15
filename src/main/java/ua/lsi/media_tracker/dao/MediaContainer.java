package ua.lsi.media_tracker.dao;

import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;

import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public interface MediaContainer {

    Map<String, List<Media>> tryLoadFromSavedResource();

    Map<String, List<Media>> loadInformation();

    String saveMediaMap(SaveType saveType, Map<String, List<Media>> mediaMap);

    default String removeMedia(Media media) {
        return "";
    }

    default String removeSection(String section) {
        return "";
    }
}
