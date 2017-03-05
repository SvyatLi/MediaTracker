package ua.lsi.media_tracker.dao;

import javafx.collections.FXCollections;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.Main;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.model.Section;
import ua.lsi.media_tracker.repository.MediaRepository;
import ua.lsi.media_tracker.repository.SectionRepository;
import ua.lsi.media_tracker.utils.FileParserAndSaver;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Component
@Log4j
public class SqliteMediaContainer implements MediaContainer {

    private Map<String, List<Media>> mediaMap;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private FileParserAndSaver fileParserAndSaver;

    @Autowired
    private Messages messages;


    @Override
    public String tryLoadFromSavedResource() {
        return loadInformation();
    }

    @Override
    public String loadInformation() {
        mediaMap = new LinkedHashMap<>();
        Iterable<Media> medias = mediaRepository.findAll();
        for (Media media : medias) {
            List<Media> mediaList = mediaMap.getOrDefault(media.getSection().getName(), FXCollections.observableArrayList());
            mediaList.add(media);
            mediaMap.put(media.getSection().getName(), mediaList);
        }
        if (!mediaMap.isEmpty()) {
            return messages.getMessage(MessageCode.LOAD_SQLITE_SUCCESSFUL);
        }
        return messages.getMessage(MessageCode.LOAD_SQLITE_UNSUCCESSFUL);
    }

    @Override
    public String loadInformationFromFile(File file) {
        return parseFileToMap(file);
    }

    @Override
    public Map<String, List<Media>> getSectionToMediaMap() {
        return mediaMap;
    }

    @Override
    public String saveMediaMap(SaveType saveType) {
        String message = null;
        if (saveType == SaveType.AUTOMATIC) {
            message = saveMediaToDB();
        } else if (saveType == SaveType.MANUAL) {
            message = "Import to file not implemented yet";
        }
        return message;
    }

    private String saveMediaToDB() {
        String message;
        try {
            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                Section section = sectionRepository.findSectionByName(entry.getKey());
                if (section == null) {
                    Section createdSection = Section.builder()
                            .name(entry.getKey())
                            .medias(new LinkedHashSet<>(entry.getValue()))
                            .build();
                    sectionRepository.save(createdSection);
                    entry.getValue().forEach(media -> media.setSection(createdSection));
                } else {
                    entry.getValue().forEach(media -> media.setSection(section));
                }
                mediaRepository.save(entry.getValue());
            }
            Main.mediaTrackerController.setModified(false);
            message = messages.getMessage(MessageCode.SAVE_SQLITE_SUCCESSFUL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            message = messages.getMessage(MessageCode.SAVE_SQLITE_UNSUCCESSFUL);
        }
        return message;
    }

    private String parseFileToMap(File file) {
        mediaMap = fileParserAndSaver.getMapOfMediaFromFile(file);
        if (file != null && file.exists()) {
            return messages.getMessageRelatedToFile(MessageCode.LOAD_SUCCESSFUL, file);
        } else {
            return messages.getMessage(MessageCode.LOAD_UNSUCCESSFUL);
        }
    }


}
