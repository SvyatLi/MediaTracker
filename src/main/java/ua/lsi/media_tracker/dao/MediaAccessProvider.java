package ua.lsi.media_tracker.dao;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.SaveType;
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
@Log4j
public class MediaAccessProvider {

    @Autowired
    private FileMediaContainer fileMediaContainer;

    @Autowired
    private SqliteMediaContainer sqliteMediaContainer;

    @Autowired
    private Messages messages;

    private Map<String, List<Media>> mediaMap;

    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    public String tryLoadFromSavedResource() {
        String message;
        log.info("Loading from saved resource");
        mediaMap = getMediaContainer().tryLoadFromSavedResource();
        if (!mediaMap.isEmpty()) {
            message = createMessage(AUTO_LOAD_SUCCESSFUL);
        } else {
            message = createMessage(AUTO_LOAD_UNSUCCESSFUL);
            log.error(message);
        }

        log.info(message);
        return message;
    }

    public String loadInformation() {
        String message;

        mediaMap = getMediaContainer().loadInformation();

        if (!mediaMap.isEmpty()) {
            message = createMessage(LOAD_SUCCESSFUL);
        } else {
            message = createMessage(LOAD_UNSUCCESSFUL);
            log.error(message);
        }
        log.info(message);
        return message;
    }

    public String loadInformationFromFile(File file) {
        String message;
        mediaMap = fileMediaContainer.loadInformationFromFile(file);
        if (!mediaMap.isEmpty()) {
            message = createMessage(LOAD_SUCCESSFUL, file);
        } else {
            message = createMessage(LOAD_UNSUCCESSFUL);
            log.error(message);
        }
        log.info(message);
        return message;
    }

    public String saveMediaMap() {
        String message = getMediaContainer().saveMediaMap(SaveType.AUTOMATIC, mediaMap);
        log.info(message);
        return message;
    }

    public String saveMediaMapToFile() {
        String message = fileMediaContainer.saveMediaMap(SaveType.MANUAL, mediaMap);
        log.info(message);
        return message;
    }

    public String removeMedia(Media media) {
        return getMediaContainer().removeMedia(media);
    }

    public String removeSection(String section) {
        return getMediaContainer().removeSection(section);
    }

    private MediaContainer getMediaContainer() {
        return sqliteMediaContainer;
    }

    private String createMessage(MessageCode code, File file) {
        return messages.getMessageRelatedToFile(code, file);
    }

    private String createMessage(MessageCode code) {
        String message = messages.getMessage(code);
        log.debug(message);
        return message;
    }
}
