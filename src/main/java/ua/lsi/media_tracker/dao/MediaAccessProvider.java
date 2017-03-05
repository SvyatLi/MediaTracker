package ua.lsi.media_tracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.enums.StorageType;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
import java.util.List;
import java.util.Map;

import static ua.lsi.media_tracker.enums.MessageCode.*;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Component
public class MediaAccessProvider {

    @Autowired
    private FileMediaContainer fileMediaContainer;

    @Autowired
    private SqliteMediaContainer sqliteMediaContainer;

    @Autowired
    private Settings settings;

    @Autowired
    private Messages messages;

    private Map<String, List<Media>> mediaMap;

    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    public String tryLoadFromSavedResource() {
        String returnedMessage;
        mediaMap = getMediaContainer().tryLoadFromSavedResource();
        if (settings.getAutomaticLoadEnabled()) {
            if (!mediaMap.isEmpty()) {
                returnedMessage = createMessage(AUTO_LOAD_SUCCESSFUL);
            } else {
                returnedMessage = createMessage(AUTO_LOAD_UNSUCCESSFUL);
            }
        } else {
            returnedMessage = createMessage(AUTO_LOAD_DISABLED);
        }
        return returnedMessage;
    }

    public String loadInformation() {
        String returnedMessage;

        mediaMap = getMediaContainer().loadInformation();

        if (!mediaMap.isEmpty()) {
            returnedMessage = createMessage(LOAD_SUCCESSFUL);
        } else {
            returnedMessage = createMessage(LOAD_UNSUCCESSFUL);
        }

        return returnedMessage;
    }

    public String loadInformationFromFile(File file) {
        String message;
        mediaMap = fileMediaContainer.loadInformationFromFile(file);
        if (!mediaMap.isEmpty()) {
            message = createMessage(LOAD_SUCCESSFUL, file);
        } else {
            message = createMessage(LOAD_UNSUCCESSFUL);
        }
        return message;
    }

    public String saveMediaMap(SaveType saveType) {
        return null;
    }

    private MediaContainer getMediaContainer() {
        return getMediaContainer(settings.getStorageType());
    }

    private String createMessage(MessageCode code, File file) {
        return messages.getMessageRelatedToFile(code, file);
    }

    private String createMessage(MessageCode code) {
        return messages.getMessage(code);
    }

    private MediaContainer getMediaContainer(StorageType storageType) {
        switch (storageType) {
            case FILE:
                return fileMediaContainer;
            case SQLITE:
                return sqliteMediaContainer;
            default:
                return fileMediaContainer;
        }
    }
}
