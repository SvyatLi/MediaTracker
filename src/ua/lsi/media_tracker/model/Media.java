package ua.lsi.media_tracker.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Media {

    //    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private String section;

    private String name;

    private int season;

    private int episode;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public void change(String property, String sign) {
        int change = 1;
        if ("-".equals(sign)) {
            change = -change;
        }
        if ("Episode".equals(property)) {
            episode += change;
        } else if ("Season".equals(property)) {
            season += change;
        }
    }

    @Override
    public String toString() {
        return "Media{" +
                "section='" + section + '\'' +
                ", name='" + name + '\'' +
                ", season=" + season +
                ", episode=" + episode +
                '}';
    }
}
