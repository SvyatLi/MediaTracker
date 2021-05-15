package ua.lsi.media_tracker.dao;

import javafx.collections.FXCollections;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.model.Section;
import ua.lsi.media_tracker.repository.MediaRepository;
import ua.lsi.media_tracker.repository.SectionRepository;

import java.util.ArrayList;
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
@Log4j
@Component
@RequiredArgsConstructor
public class SqliteMediaContainer implements MediaContainer {

    public static final String DEFAULT_SECTION_NAME = "Default";
    private final  MediaRepository mediaRepository;
    private final  SectionRepository sectionRepository;
    private final  Messages messages;

    @Override
    public Map<String, List<Media>> loadInformation() {
        Map<String, List<Media>> mediaMap = new LinkedHashMap<>();
        sectionRepository.findAll().stream()
                .map(Section::getName)
                .forEach(s -> mediaMap.put(s, FXCollections.observableArrayList()));

        mediaRepository.findAll()
                .forEach(media -> {
                    String sectionName = Optional.ofNullable(media.getSection())
                            .map(Section::getName)
                            .orElse(DEFAULT_SECTION_NAME);
                    List<Media> mediaList = mediaMap.getOrDefault(sectionName, FXCollections.observableArrayList());
                    mediaList.add(media);
                    mediaMap.put(sectionName, mediaList);
                });
        mediaMap.values().forEach(mediaList -> mediaList.sort(Comparator.comparingInt(Media::getPosition)));

        return mediaMap;
    }

    @Override
    public String saveMediaMap(Map<String, List<Media>> mediaMap) {
        return saveMediaToDB(mediaMap);
    }

    @Override
    public void removeMedia(Media media) {
        if (media.getId() != null) {
            mediaRepository.delete(media);
        }
    }

    @Override
    public void removeSection(String section) {
        sectionRepository.delete(section);
    }

    private String saveMediaToDB(Map<String, List<Media>> mediaMap) {
        String message;
        try {
            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                Section section = Optional.ofNullable(sectionRepository.findSectionByName(entry.getKey()))
                        .orElseGet(() -> sectionRepository.create(entry.getKey()));
                List<Media> mediaList = entry.getValue();
                mediaList.forEach(media -> media.setSection(section));
                List<Media> savedMediaList = new ArrayList<>();

                for (int i = 0; i < mediaList.size(); i++) {
                    Media media = mediaList.get(i);
                    media.setPosition(i);
                    savedMediaList.add(mediaRepository.save(media));
                }
                mediaList.clear();
                mediaList.addAll(savedMediaList);
            }
            message = messages.getMessage(MessageCode.SAVE_SQLITE_SUCCESSFUL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            message = messages.getMessage(MessageCode.SAVE_SQLITE_UNSUCCESSFUL) + " " + e.getMessage();
        }
        log.info(message);
        return message;
    }
}
