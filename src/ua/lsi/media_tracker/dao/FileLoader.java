package ua.lsi.media_tracker.dao;

import javafx.stage.FileChooser;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileLoader implements MediaContainer {

    private File file = null;
    private Map<String, List<Media>> mediaMap = null;

    @Override
    public void tryLoadFromSavedResource() {
        try {
            Path savedFilePath = Paths.get(System.getProperty("java.io.tmpdir"), "MediaTracker.txt");
            File savedFile = savedFilePath.toFile();
            if (savedFile!=null && savedFile.exists()) {
                List<String> savedLines = Files.readAllLines(savedFilePath, Charset.forName("UTF-8"));
                if (savedLines != null && !savedLines.isEmpty()) {
                    file = new File(savedLines.get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseFileToMap(file);
    }

    @Override
    public void loadInformation() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        parseFileToMap(file);
        savePathToFile(file);
    }

    private void parseFileToMap(File file) {
        if (file != null && file.exists()) {
            FileParser fileParser = new FileParser();
            mediaMap = fileParser.getMapOfMediaFromFile(file);
        } else {
            mediaMap = Collections.EMPTY_MAP;
        }
    }

    private void savePathToFile(File file) {
        if (file != null && file.exists()) {
            try {
                Path savedFile = Paths.get(System.getProperty("java.io.tmpdir"), "MediaTracker.txt");
                List<String> lines = Collections.singletonList(file.getAbsolutePath());
                Files.write(savedFile, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
