package ua.lsi.media_tracker.dao;

import javafx.stage.FileChooser;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileLoader implements MediaContainer {

    private File file = null;
    private List<Media> medias = null;

    @Override
    public void init() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        System.out.println(file);
        if (file != null) {
            FileParser fileParser = new FileParser();
            medias = fileParser.parseMediaFromFile(file);
        } else {
            medias = Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Media> getAll() {
        return medias;
    }

    @Override
    public void saveAll() {
        System.out.println(file);
    }
}
