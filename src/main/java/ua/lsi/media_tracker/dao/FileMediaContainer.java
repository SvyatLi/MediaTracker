package ua.lsi.media_tracker.dao;


import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.model.Settings;
import ua.lsi.media_tracker.utils.FileParserAndSaver;
import ua.lsi.media_tracker.utils.FileProvider;
import ua.lsi.media_tracker.utils.MessageCreator;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import static ua.lsi.media_tracker.enums.MessageCode.*;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileMediaContainer implements MediaContainer {

    private File file;
    private Map<String, List<Media>> mediaMap;

    private FileProvider fileProvider;

    @Override
    public String tryLoadFromSavedResource() {
        String returnedMessage;
        file = Settings.getInstance().getDefaultInfoFile();
        parseFileToMap(file);
        if (Settings.getInstance().isAutomaticLoadEnabled()) {
            if (file != null && file.exists()) {
                returnedMessage = createMessage(AUTO_LOAD_SUCCESSFUL, file);
            } else {
                returnedMessage = createMessage(AUTO_LOAD_UNSUCCESSFUL);
            }
        } else {
            returnedMessage = createMessage(AUTO_LOAD_DISABLED);
        }
        return returnedMessage;
    }

    @Override
    public String loadInformation() {
        file = getFileProvider().getFileForLoad();
        parseFileToMap(file);
        String returnedMessage;
        if (file != null && file.exists()) {
            returnedMessage = createMessage(LOAD_SUCCESSFUL, file);
        } else {
            returnedMessage = createMessage(LOAD_UNSUCCESSFUL);
        }
        return returnedMessage;
    }

    @Override
    public Map<String, List<Media>> getAll() {
        return mediaMap;
    }

    @Override
    public String saveAll() {
        File fileToSaveTo = getFileProvider().getFileForSave(file);
        String returnedMessage;
        if (fileToSaveTo != null && fileToSaveTo.exists()) {
            FileParserAndSaver fileParserAndSaver = new FileParserAndSaver();
            fileParserAndSaver.saveMapToFile(mediaMap, fileToSaveTo);
            returnedMessage = createMessage(MessageCode.SAVE_SUCCESSFUL, fileToSaveTo);
        } else {
            returnedMessage = createMessage(MessageCode.SAVE_UNSUCCESSFUL);
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

    private String createMessage(MessageCode code, File file) {
        return MessageCreator.getInstance().getMessageRelatedToCodeAndFile(code, file);
    }

    private String createMessage(MessageCode code) {
        return MessageCreator.getInstance().getMessageRelatedToCode(code);
    }

    private FileProvider getFileProvider() {
        if (fileProvider==null){
            fileProvider = FileProvider.getInstance();
        }
        return fileProvider;
    }

    public void setFileProvider(FileProvider fileProvider){
        this.fileProvider = fileProvider;
    }
}
