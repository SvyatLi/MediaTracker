package ua.lsi.media_tracker.repository;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.ConnectionManager;
import ua.lsi.media_tracker.model.Media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSI on 06.07.2017.
 *
 * @author LSI
 */
@Log4j
@Component
public class SqliteMediaRepository implements MediaRepository {

    private static final String INSERT_MEDIA = "INSERT INTO 'main'.'media' ('position', 'name', 'season', 'episode', 'section_id') VALUES ( ? , ?, ?, ?, ?);";
    private static final String UPDATE_MEDIA = "UPDATE main.media SET \"position\"=?, \"name\"=?, \"season\"=?, \"episode\"=?, \"section_id\"=? WHERE (\"id\"=?);";
    private static final String DELETE_MEDIA = "DELETE FROM main.media WHERE (id=?);";

    @Autowired
    ConnectionManager cm;

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public boolean save(Media entity) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement(INSERT_MEDIA);
            ps.setInt(1, entity.getPosition());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getSeason());
            ps.setInt(4, entity.getEpisode());
            ps.setLong(5, entity.getSection().getId());
            int res = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean update(Media entity) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement(UPDATE_MEDIA);
            ps.setInt(1, entity.getPosition());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getSeason());
            ps.setInt(4, entity.getEpisode());
            ps.setLong(5, entity.getSection().getId());
            ps.setLong(6, entity.getId());
            int res = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public boolean save(Iterable<Media> entities) {
        boolean result = true;
        for (Media entity : entities) {
            if (entity.getId() == null) {
                result = result && save(entity);
            } else {
                result = result && update(entity);
            }
        }
        return result;
    }

    @Override
    public Iterable<Media> findAll() {
        List<Media> medias = new ArrayList<>();
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("SELECT * FROM 'main'.'media';");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medias.add(createMedia(rs));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return medias;
    }

    @Override
    public boolean delete(Media entity) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement(DELETE_MEDIA);

            ps.setInt(1, entity.getId());
            int res = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Media createMedia(ResultSet rs) throws SQLException {
        return Media.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .season(rs.getInt("season"))
                .episode(rs.getInt("episode"))
                .position(rs.getInt("position"))
                .section(sectionRepository.findSectionById(rs.getLong("section_id")))
                .build();
    }

}
