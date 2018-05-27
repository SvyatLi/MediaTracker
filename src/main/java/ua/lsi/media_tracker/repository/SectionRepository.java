package ua.lsi.media_tracker.repository;

import ua.lsi.media_tracker.model.Section;

import java.util.List;

/**
 * Created by LSI on 04.03.2017.
 *
 * @author LSI
 */
public interface SectionRepository {

    Section findSectionByName(String name);

    Section findSectionById(Long id);

    List<Section> findAll();

    Section save(Section entity);

    Section create(String name);

    boolean delete(String name);

}
