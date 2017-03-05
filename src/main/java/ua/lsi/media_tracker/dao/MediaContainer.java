package ua.lsi.media_tracker.dao;

import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
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

    Map<String, List<Media>> loadInformationFromFile(File file);

    String saveMediaMap(SaveType saveType, Map<String, List<Media>> mediaMap);
}
