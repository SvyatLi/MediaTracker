package ua.lsi.media_tracker.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParserAndSaver;

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
@Component
public class FileMediaContainer implements MediaContainer {

    private File file;
    private Map<String, List<Media>> mediaMap;

    private FileProvider fileProvider;
    private Messages messages;
    private Settings settings;
    private FileParserAndSaver fileParserAndSaver;

    @Override
    public String tryLoadFromSavedResource() {
        String returnedMessage;
        file = settings.getDefaultInfoFile();
        if (settings.isAutomaticLoadEnabled()) {
            if (file != null && file.exists()) {
                parseFileToMap(file);
                returnedMessage = createMessage(AUTO_LOAD_SUCCESSFUL, file);
            } else {
                mediaMap = Collections.EMPTY_MAP;
                returnedMessage = createMessage(AUTO_LOAD_UNSUCCESSFUL);
            }
        } else {
            mediaMap = Collections.EMPTY_MAP;
            returnedMessage = createMessage(AUTO_LOAD_DISABLED);
        }
        return returnedMessage;
    }

    @Override
    public String loadInformation() {
        file = fileProvider.getFileForLoad();
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
    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    @Override
    public String saveMediaMap() {
        File fileToSaveTo = fileProvider.getFileForSave(file);
        String returnedMessage;
        if (fileToSaveTo != null && fileToSaveTo.exists()) {
            fileParserAndSaver.saveMapToFile(mediaMap, fileToSaveTo);
            returnedMessage = createMessage(MessageCode.SAVE_SUCCESSFUL, fileToSaveTo);
        } else {
            returnedMessage = createMessage(MessageCode.SAVE_UNSUCCESSFUL);
        }
        return returnedMessage;
    }

    private void parseFileToMap(File file) {
        if (file != null && file.exists()) {
            mediaMap = fileParserAndSaver.getMapOfMediaFromFile(file);
        } else {
            mediaMap = Collections.EMPTY_MAP;
        }
    }

    private String createMessage(MessageCode code, File file) {
        return messages.getMessageRelatedToFile(code, file);
    }

    private String createMessage(MessageCode code) {
        return messages.getMessage(code);
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Autowired
    public void setFileProvider(FileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Autowired
    public void setFileParserAndSaver(FileParserAndSaver fileParserAndSaver) {
        this.fileParserAndSaver = fileParserAndSaver;
    }
}
