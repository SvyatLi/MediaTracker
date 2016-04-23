package ua.lsi.media_tracker.dao;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParserAndSaver;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
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
    private static Logger LOG = Logger.getLogger(FileMediaContainer.class);
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
            parseFileToMap(file);
            if (file != null && file.exists()) {
                returnedMessage = createMessage(AUTO_LOAD_SUCCESSFUL, file);
            } else {
                returnedMessage = createMessage(AUTO_LOAD_UNSUCCESSFUL);
            }
        } else {
            mediaMap = new LinkedHashMap<>();
            returnedMessage = createMessage(AUTO_LOAD_DISABLED);
        }
        return returnedMessage;
    }

    @Override
    public String loadInformation() {
        return loadInformationFromFile(null);
    }

    @Override
    public String loadInformationFromFile(File file) {
        if (file == null) {
            file = fileProvider.getFileForLoad();
        }
        this.file = file;

        String returnedMessage = parseFileToMap(file);
        return returnedMessage;
    }

    @Override
    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    @Override
    public String saveMediaMap(SaveType saveType) {
        File fileToSaveTo = null;
        switch (saveType) {
            case AUTOMATIC:
                fileToSaveTo = file;
                break;
            case MANUAL:
            default:
                fileToSaveTo = fileProvider.getFileForSave(file);
        }
        String returnedMessage;
        if (checkFileExistsAndCreateIfNot(fileToSaveTo)) {
            fileParserAndSaver.saveMapToFile(mediaMap, fileToSaveTo);
            returnedMessage = createMessage(MessageCode.SAVE_SUCCESSFUL, fileToSaveTo);
        } else {
            returnedMessage = createMessage(MessageCode.SAVE_UNSUCCESSFUL);
        }
        return returnedMessage;
    }

    private String parseFileToMap(File file) {
        mediaMap = fileParserAndSaver.getMapOfMediaFromFile(file);

        if (file != null && file.exists()) {
            return createMessage(LOAD_SUCCESSFUL, file);
        } else {
            return createMessage(LOAD_UNSUCCESSFUL);
        }
    }

    private String createMessage(MessageCode code, File file) {
        return messages.getMessageRelatedToFile(code, file);
    }

    private String createMessage(MessageCode code) {
        return messages.getMessage(code);
    }

    private boolean checkFileExistsAndCreateIfNot(File file) {
        if (file != null) {
            boolean fileExist = true;
            if (!file.exists()) {
                fileExist = false;
                try {
                    fileExist = file.createNewFile();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
            return fileExist;
        }
        return false;
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
