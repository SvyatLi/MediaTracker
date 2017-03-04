package ua.lsi.media_tracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.repository.MediaRepository;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Component
public class SqliteMediaContainer implements MediaContainer {

    private Map<String, List<Media>> mediaMap;

    @Autowired
    private MediaRepository repository;

    @Override
    public String tryLoadFromSavedResource() {
        return loadInformation();
    }

    @Override
    public String loadInformation() {
        mediaMap = new LinkedHashMap<>();
        return null;
    }

    @Override
    public String loadInformationFromFile(File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    @Override
    public String saveMediaMap(SaveType saveType) {
        return null;
    }
}
