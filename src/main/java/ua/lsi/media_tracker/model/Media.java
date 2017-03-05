package ua.lsi.media_tracker.model;

import lombok.*;

import javax.persistence.*;
import java.text.NumberFormat;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Data
@Entity
@Builder
@EqualsAndHashCode(exclude = "section")
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    private String name;

    private int season;

    private int episode;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

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
