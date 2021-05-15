package ua.lsi.media_tracker.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
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
@Log4j
@Component
@RequiredArgsConstructor
public class MediaAccessProvider {

    private final FileMediaContainer fileMediaContainer;
    private final SqliteMediaContainer sqliteMediaContainer;
    private final Messages messages;

    private Map<String, List<Media>> mediaMap;

    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    public String tryLoadFromSavedResource() {
        String message;
        log.info("Loading from saved resource");
        mediaMap = sqliteMediaContainer.loadInformation();
        if (!mediaMap.isEmpty()) {
            message = createSuccessMessage(AUTO_LOAD_SUCCESSFUL);
        } else {
            message = createSuccessMessage(AUTO_LOAD_UNSUCCESSFUL);
            log.error(message);
        }

        log.info(message);
        return message;
    }

    public String loadInformationFromFile(File file) {
        String message;
        mediaMap = fileMediaContainer.loadInformationFromFile(file);
        if (!mediaMap.isEmpty()) {
            message = createSuccessMessage(file);
        } else {
            message = createSuccessMessage(LOAD_UNSUCCESSFUL);
            log.error(message);
        }
        log.info(message);
        return message;
    }

    public String saveMediaMap() {
        return sqliteMediaContainer.saveMediaMap(mediaMap);
    }

    public String saveMediaMapToFile() {
        String message = fileMediaContainer.saveToFile(mediaMap);
        log.info(message);
        return message;
    }

    public void removeMedia(Media media) {
        sqliteMediaContainer.removeMedia(media);
    }

    public void removeSection(String section) {
        sqliteMediaContainer.removeSection(section);
    }

    private String createSuccessMessage(File file) {
        return messages.getMessageRelatedToFile(LOAD_SUCCESSFUL, file);
    }

    private String createSuccessMessage(MessageCode code) {
        String message = messages.getMessage(code);
        log.debug(message);
        return message;
    }
}
