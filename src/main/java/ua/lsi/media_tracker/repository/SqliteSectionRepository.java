package ua.lsi.media_tracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.ConnectionManager;
import ua.lsi.media_tracker.model.Section;

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
public class SqliteSectionRepository implements SectionRepository {


    private final ConnectionManager cm;

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
    public List<Section> findAll() {
        List<Section> results = new ArrayList<>();
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("SELECT * FROM 'main'.'section';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(createSection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        return results;
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

    @Override
    public boolean delete(String section) {
        try {
            PreparedStatement ps = cm.getConnection().prepareStatement("DELETE FROM 'main'.'section' WHERE (name=?);");

            ps.setString(1, section);
            int res = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Section createSection(ResultSet rs) throws SQLException {
        return Section.builder()
                .name(rs.getString("name"))
                .id(rs.getLong("id"))
                .build();

    }
}
