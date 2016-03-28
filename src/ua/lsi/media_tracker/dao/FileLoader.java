package ua.lsi.media_tracker.dao;

import javafx.stage.FileChooser;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.model.MessageHolder;
import ua.lsi.media_tracker.utils.FileParserAndSaver;
import ua.lsi.media_tracker.utils.MessageCreator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static ua.lsi.media_tracker.enums.MessageCode.*;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileLoader implements MediaContainer {

    private File file = null;
    private Map<String, List<Media>> mediaMap = null;

    @Override
    public String tryLoadFromSavedResource() {
        try {
            Path savedFilePath = getTempFilePath();
            File savedFile = savedFilePath.toFile();
            if (savedFile != null && savedFile.exists()) {
                List<String> savedLines = Files.readAllLines(savedFilePath, Charset.forName("UTF-8"));
                if (savedLines != null && !savedLines.isEmpty()) {
                    file = new File(savedLines.get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseFileToMap(file);
        String returnedMessage;
        if (file == null || !file.exists()) {
            returnedMessage = createMessage(AUTO_LOAD_UNSUCCESSFUL, file);
        } else {
            returnedMessage = createMessage(AUTO_LOAD_SUCCESSFUL, file);
        }
        return returnedMessage;
    }

    @Override
    public String loadInformation() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        parseFileToMap(file);
        savePathToFile(file);
        String returnedMessage;
        if (file == null || !file.exists()) {
            returnedMessage = createMessage(LOAD_UNSUCCESSFUL, file);
        } else {
            returnedMessage = createMessage(LOAD_SUCCESSFUL, file);
        }
        return returnedMessage;
    }

    private void parseFileToMap(File file) {
        if (file != null && file.exists()) {
            FileParserAndSaver fileParserAndSaver = new FileParserAndSaver();
            mediaMap = fileParserAndSaver.getMapOfMediaFromFile(file);
        } else {
            mediaMap = Collections.EMPTY_MAP;
        }
    }

    private void savePathToFile(File file) {
        if (file != null && file.exists()) {
            try {
                Path savedFilePath = getTempFilePath();
                List<String> lines = Collections.singletonList(file.getAbsolutePath());
                Files.write(savedFilePath, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Path getTempFilePath() {
        return Paths.get(System.getProperty("java.io.tmpdir"), MessageHolder.getInstance().getMessage(TEMP_FILENAME));
    }

    private String createMessage(MessageCode code, File file) {
        return MessageCreator.getInstance().getMessageRelatedToCodeAndFile(code, file);
    }

    @Override
    public Map<String, List<Media>> getAll() {
        return mediaMap;
    }

    @Override
    public String saveAll() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file.getParentFile());
        fileChooser.setInitialFileName(file.getName());
        File fileToSaveTo = fileChooser.showSaveDialog(null);
        String returnedMessage;
        if (fileToSaveTo != null && fileToSaveTo.exists()) {
            FileParserAndSaver fileParserAndSaver = new FileParserAndSaver();
            fileParserAndSaver.saveMapToFile(mediaMap, fileToSaveTo);
            returnedMessage = createMessage(MessageCode.SAVE_SUCCESSFUL, fileToSaveTo);
        } else {
            returnedMessage = createMessage(MessageCode.SAVE_UNSUCCESSFUL, fileToSaveTo);
        }
        return returnedMessage;
    }
}
