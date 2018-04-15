package ua.lsi.media_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;

/**
 * Created by LSI on 05.03.2017.
 *
 * @author LSI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    private Long id;

    private String name;
}
