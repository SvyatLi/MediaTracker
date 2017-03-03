package ua.lsi.media_tracker.model;

import lombok.Data;

import java.text.NumberFormat;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Data
public class Media {

    private final static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    private String name;

    private int season;

    private int episode;

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
        NUMBER_FORMAT.setMinimumIntegerDigits(2);
        return name + " - " + "s" + NUMBER_FORMAT.format(season) + "e" + NUMBER_FORMAT.format(episode);
    }
}
