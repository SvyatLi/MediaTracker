package ua.lsi.media_tracker.model;

import javafx.beans.property.SimpleStringProperty;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Media {

    private String name;

    private int season;

    private int episode;

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
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        return  name + " - " + "s" + nf.format(season) +"e" + nf.format(episode);
    }
}
