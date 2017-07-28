package ua.lsi.media_tracker.repository;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.ConnectionManager;
import ua.lsi.media_tracker.model.Section;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by LSI on 06.07.2017.
 *
 * @author LSI
 */
@Component
@Log4j
public class SqliteSectionRepository implements SectionRepository {


    @Autowired
    ConnectionManager cm;

    @Override
    public Section findSectionByName(String name) {
        Section result = null;
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("SELECT * FROM 'main'.'section' WHERE name=?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = createSection(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Section findSectionById(Long id) {
        Section result = null;
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("SELECT * FROM 'main'.'section' WHERE id=?;");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = createSection(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public Section save(Section entity) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("INSERT INTO 'main'.'section' ('name') VALUES ( ?);");
            ps.setString(1, entity.getName());
            ps.executeUpdate();
            return findSectionByName(entity.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Section create(String name) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("INSERT INTO 'main'.'section' ('name') VALUES ( ?);");
            ps.setString(1, name);
            ps.executeUpdate();
            return findSectionByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private Section createSection(ResultSet rs) throws SQLException {
        return Section.builder()
                .name(rs.getString("name"))
                .id(rs.getLong("id"))
                .build();

    }
}
