package ua.lsi.media_tracker.repository;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SqliteMediaRepository implements MediaRepository {

    private static final String INSERT_MEDIA = "INSERT INTO 'main'.'media' ('position', 'name', 'season', 'episode', 'section_id') VALUES ( ? , ?, ?, ?, ?);";
    private static final String UPDATE_MEDIA = "UPDATE main.media SET \"position\"=?, \"name\"=?, \"season\"=?, \"episode\"=?, \"section_id\"=? WHERE (\"id\"=?);";
    private static final String DELETE_MEDIA = "DELETE FROM main.media WHERE (id=?);";
    private static final String SELECT_ALL_MEDIA = "SELECT * FROM 'main'.'media';";
    private static final String SELECT_MEDIA_BY_NAME = "SELECT * FROM 'main'.'media' WHERE name=?;";

    private final ConnectionManager cm;
    private final SectionRepository sectionRepository;

    @Override
    public Media save(Media entity) {
        try {
            PreparedStatement ps;
            if (entity.getId() == null) {
                ps = prepareUpdateStatement(entity, INSERT_MEDIA);
            } else {
                ps = prepareUpdateStatement(entity, UPDATE_MEDIA);
                ps.setLong(6, entity.getId());
            }
            ps.executeUpdate();
            PreparedStatement sps = cm.getConnection().prepareStatement(SELECT_MEDIA_BY_NAME);
            sps.setString(1, entity.getName());
            ResultSet rs = sps.executeQuery();
            return createMedia(rs);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private PreparedStatement prepareUpdateStatement(Media entity, String statement) throws SQLException {
        PreparedStatement ps = cm.getConnection().prepareStatement(statement);
        ps.setInt(1, entity.getPosition());
        ps.setString(2, entity.getName());
        ps.setInt(3, entity.getSeason());
        ps.setInt(4, entity.getEpisode());
        ps.setLong(5, entity.getSection().getId());
        return ps;
    }

    @Override
    public Iterable<Media> findAll() {
        List<Media> medias = new ArrayList<>();
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement(SELECT_ALL_MEDIA);

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
