package ua.lsi.media_tracker.creators;

import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.utils.SettingsProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by LSI on 19.07.2017.
 *
 * @author LSI
 */
@Component
public class ConnectionManager {

    private Connection connection = null;

    public ConnectionManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + SettingsProvider.getUserDataDirectory() + "media_tracker_test.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
