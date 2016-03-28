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

    String tryLoadFromSavedResource();

    String loadInformation();

    Map<String, List<Media>> getAll();

    String saveAll();
}
