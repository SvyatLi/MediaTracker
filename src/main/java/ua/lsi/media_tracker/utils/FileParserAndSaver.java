package ua.lsi.media_tracker.utils;

import javafx.collections.FXCollections;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Component
@Log4j
public class FileParserAndSaver {
    private final static String sectionStarter = "//";
    private final static String matcher = ".*\\s\\-\\ss\\d*e\\d*";
    private final static String matcherSeparator = "\\s\\-\\s";
    private Messages messages;

    public Map<String, List<Media>> getMapOfMediaFromFile(File file) {
        Map<String, List<Media>> mediaMap = new LinkedHashMap<>();
        String currentSection = messages.getMessage(MessageCode.DEFAULT_SECTION);
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
                String line = br.readLine();
                while (line != null) {
                    if (line.contains(sectionStarter)) {
                        currentSection = line.substring(line.indexOf(sectionStarter) + sectionStarter.length());
                        line = br.readLine();
                        continue;
                    }
                    Media media = parseMediaFromString(line);
                    if (media != null) {
                        List<Media> mediaList = mediaMap.getOrDefault(currentSection, FXCollections.observableArrayList());
                        mediaList.add(media);
                        mediaMap.put(currentSection, mediaList);
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return mediaMap;
    }

    public String saveMapToFile(Map<String, List<Media>> mediaMap, File file) {
        String statusMessage = messages.getMessage(MessageCode.SAVE_NOT_SAVED);
        boolean somethingSaved = false;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"))) {
            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                bw.newLine();
                bw.write("//" + entry.getKey());
                bw.newLine();
                for (Media media : entry.getValue()) {
                    bw.write(media.toString());
                    bw.newLine();
                    somethingSaved = true;
                }
            }
            if (somethingSaved) {
                statusMessage = messages.getMessageRelatedToFile(MessageCode.SAVE_SUCCESSFUL, file);
            }
        } catch (IOException e) {
            log.error(e);
            statusMessage = messages.getMessage(MessageCode.SAVE_ERROR);
        }
        return statusMessage;
    }

    private Media parseMediaFromString(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        Media media = new Media();
        boolean match = line.matches(matcher);
        if (match) {
            String[] mediaParts = line.split(matcherSeparator);
            media.setName(mediaParts[0]);
            String[] seasonAndEpisode = mediaParts[1].split("s")[1].split("e");
            Integer season = Integer.parseInt(seasonAndEpisode[0], 10);
            Integer episode = Integer.parseInt(seasonAndEpisode[1], 10);
            media.setSeason(season);
            media.setEpisode(episode);
        } else {
            media.setName(line);
        }
        return media;
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
