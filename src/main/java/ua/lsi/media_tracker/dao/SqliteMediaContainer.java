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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Component
@Log4j
public class SqliteMediaContainer implements MediaContainer {

    public static final String DEFAULT_SECTION_NAME = "Default";
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private Messages messages;


    @Override
    public Map<String, List<Media>> tryLoadFromSavedResource() {
        return loadInformation();
    }

    @Override
    public Map<String, List<Media>> loadInformation() {
        Map<String, List<Media>> mediaMap = new LinkedHashMap<>();
        Iterable<Media> medias = mediaRepository.findAll();
        for (Media media : medias) {
            String sectionName = Optional.ofNullable(media.getSection()).map(Section::getName).orElse(DEFAULT_SECTION_NAME);
            List<Media> mediaList = mediaMap.getOrDefault(sectionName, FXCollections.observableArrayList());
            mediaList.add(media);
            mediaMap.put(sectionName, mediaList);
        }
        mediaMap.values().forEach(mediaList -> mediaList.sort(Comparator.comparingInt(Media::getPosition)));
        List<Section> sections = sectionRepository.findAll();
        Map<String, List<Media>> sortedMediaMap = new LinkedHashMap<>();
        sections.stream()
                .map(Section::getName)
                .forEach(name -> sortedMediaMap.put(name, mediaMap.get(name)));
        if (mediaMap.get(DEFAULT_SECTION_NAME) != null) {
            sortedMediaMap.put(DEFAULT_SECTION_NAME, mediaMap.get(DEFAULT_SECTION_NAME));
        }

        return sortedMediaMap;
    }

    @Override
    public String saveMediaMap(SaveType saveType, Map<String, List<Media>> mediaMap) {
        String message = null;
        if (saveType == SaveType.AUTOMATIC) {
            message = saveMediaToDB(mediaMap);
        }
        log.info(message);
        return message;
    }

    @Override
    public String removeMedia(Media media) {
        if (media.getId() != null) {
            return mediaRepository.delete(media) ? "Removed" : "Error";
        }
        return "Removed from table";
    }

    @Override
    public String removeSection(String section) {
        return sectionRepository.delete(section) ? "Removed" : "Error";
    }

    private String saveMediaToDB(Map<String, List<Media>> mediaMap) {
        String message;
        try {
            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                Section section = sectionRepository.findSectionByName(entry.getKey());
                List<Media> mediaList = entry.getValue();
                if (section == null) {
                    Section createdSection = sectionRepository.create(entry.getKey());
                    mediaList.forEach(media -> {
                        media.setSection(createdSection);
                    });
                } else {
                    mediaList.forEach(media -> {
                        media.setSection(section);
                    });
                }
                for (int i = 0; i < mediaList.size(); i++) {
                    mediaList.get(i).setPosition(i);
                }
                mediaRepository.save(mediaList);
            }
            Main.mediaTrackerController.setModified(false);
            message = messages.getMessage(MessageCode.SAVE_SQLITE_SUCCESSFUL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            message = messages.getMessage(MessageCode.SAVE_SQLITE_UNSUCCESSFUL) + " " + e.getMessage();
        }
        log.info(message);
        return message;
    }
}
