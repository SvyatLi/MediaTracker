package ua.lsi.media_tracker.repository;

import ua.lsi.media_tracker.model.Section;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface SectionRepository {

    Section findSectionByName(String name);

    Section findSectionById(Long id);

    boolean save(Section entity);

    boolean save(Iterable<Section> entities);

}
