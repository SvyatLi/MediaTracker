package ua.lsi.media_tracker.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.model.Section;

import java.util.List;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface SectionRepository extends CrudRepository<Section, Long> {

    Section findSectionByName(String name);
}
