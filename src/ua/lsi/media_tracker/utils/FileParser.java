package ua.lsi.media_tracker.utils;

import ua.lsi.media_tracker.model.Media;

import java.io.*;
import java.util.*;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class FileParser {

    private final static String sectionStarter = "//";
    private final static String matcher = ".*\\s\\-\\ss\\d*e\\d*";
    private final static String matcherSeparator = "\\s\\-\\s";
    private String currentSection;

    public Map<String,List<Media>> getMapOfMediaFromFile(File file) {
        Map<String,List<Media>> mediaMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
            String line = br.readLine();
            while (line != null) {
                Media media = parseMediaFromString(line);
                if (media != null) {
                    List<Media> mediaList= mediaMap.getOrDefault(currentSection, new ArrayList<>());
                    mediaList.add(media);
                    mediaMap.put(currentSection,mediaList);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Should never happen");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaMap;
    }

    private Media parseMediaFromString(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        if (line.contains(sectionStarter)) {
            currentSection = line.substring(line.indexOf(sectionStarter)+ sectionStarter.length());
            return null;
        }
        Media media = new Media();
        media.setSection(currentSection);
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

}
