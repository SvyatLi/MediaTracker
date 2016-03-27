package ua.lsi.media_tracker.dao;

import javafx.stage.FileChooser;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParser;

import java.io.File;
import java.util.*;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileLoader implements MediaContainer {

    private File file = null;
    private Map<String,List<Media>> mediaMap = null;

    @Override
    public void init() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        System.out.println(file);
        if (file != null) {
            FileParser fileParser = new FileParser();
            mediaMap = fileParser.getMapOfMediaFromFile(file);
        } else {
            mediaMap = Collections.EMPTY_MAP;
        }
    }

    @Override
    public Map<String, List<Media>> getAll() {
        return mediaMap;
    }

    @Override
    public void saveAll() {
        System.out.println(file);
    }
}
