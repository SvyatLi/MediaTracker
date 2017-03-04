package ua.lsi.media_tracker.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int season;

    private int episode;
}
