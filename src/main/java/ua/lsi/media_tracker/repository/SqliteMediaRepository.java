package ua.lsi.media_tracker.repository;

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
@Component
public class SqliteMediaRepository implements MediaRepository {

    @Autowired
    ConnectionManager cm;

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public boolean save(Media entity) {
        try {
            //TODO add update not only insert
            PreparedStatement ps = cm.getConnection().prepareStatement("INSERT INTO 'main'.'media' ('position','name', 'season', 'episode',  'section_id') VALUES ( ? , ?, ?, ?, ?);");
            ps.setInt(1, entity.getPosition());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getSeason());
            ps.setInt(4, entity.getEpisode());
            ps.setLong(5, entity.getSection().getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Iterable<Media> entities) {
        boolean aggregatedResult = true;
        for (Media entity : entities) {
            aggregatedResult = aggregatedResult && save(entity);
        }
        return aggregatedResult;
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
            e.printStackTrace();
        }
        return medias;
    }

    private Media createMedia(ResultSet rs) throws SQLException {
        return Media.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .season(rs.getInt("season"))
                .episode(rs.getInt("episode"))
                .position(rs.getInt("position"))
                .section(sectionRepository.findSectionById(rs.getLong("section_id")))
                .build();
    }

}
