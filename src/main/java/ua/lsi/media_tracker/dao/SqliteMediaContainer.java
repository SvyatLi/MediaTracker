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

import java.util.*;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Component
@Log4j
public class SqliteMediaContainer implements MediaContainer {

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
            List<Media> mediaList = mediaMap.getOrDefault(media.getSection().getName(), FXCollections.observableArrayList());
            mediaList.add(media);
            mediaMap.put(media.getSection().getName(), mediaList);
        }
        mediaMap.values().forEach(mediaList -> mediaList.sort(Comparator.comparingInt(Media::getPosition)));

        return mediaMap;
    }

    @Override
    public String saveMediaMap(SaveType saveType, Map<String, List<Media>> mediaMap) {
        String message = null;
        if (saveType == SaveType.AUTOMATIC) {
            message = saveMediaToDB(mediaMap);
        }
        return message;
    }

    @Override
    public String removeMedia(Media media) {
        return mediaRepository.delete(media) ? "Removed" : "Error";
    }

    private String saveMediaToDB(Map<String, List<Media>> mediaMap) {
        String message;
        try {
            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                Section section = sectionRepository.findSectionByName(entry.getKey());
                List<Media> mediaList = entry.getValue();
                if (section == null) {
                    Section createdSection = Section.builder()
                            .name(entry.getKey())
                            .medias(new LinkedHashSet<>(mediaList))
                            .build();
                    sectionRepository.save(createdSection);
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
        return message;
    }
}
