package ua.lsi.media_tracker.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.Main;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParserAndSaver;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Log4j
@Component
@RequiredArgsConstructor
public class FileMediaContainer implements MediaContainer {
    private File file;

    private final FileProvider fileProvider;

    private final Messages messages;

    private final FileParserAndSaver fileParserAndSaver;

    @Override
    public Map<String, List<Media>> tryLoadFromSavedResource() {
        return new LinkedHashMap<>();
    }

    @Override
    public Map<String, List<Media>> loadInformation() {
        return loadInformationFromFile(null);
    }

    public Map<String, List<Media>> loadInformationFromFile(File file) {
        if (file == null) {
            file = fileProvider.getFileForLoad();
        }
        this.file = file;

        return parseFileToMap(file);
    }

    @Override
    public String saveMediaMap(SaveType saveType, Map<String, List<Media>> mediaMap) {
        File fileToSaveTo = fileProvider.getFileForSave(file);
        String returnedMessage;
        if (checkFileExistsAndCreateIfNot(fileToSaveTo)) {
            fileParserAndSaver.saveMapToFile(mediaMap, fileToSaveTo);
            returnedMessage = createMessage(MessageCode.SAVE_SUCCESSFUL, fileToSaveTo);
            Main.mediaTrackerController.setModified(false);
        } else {
            returnedMessage = createMessage(MessageCode.SAVE_UNSUCCESSFUL);
        }
        return returnedMessage;
    }

    private Map<String, List<Media>> parseFileToMap(File file) {
        return fileParserAndSaver.getMapOfMediaFromFile(file);
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
                    log.error(e.getMessage(), e);
                }
            }
            return fileExist;
        }
        return false;
    }
}
