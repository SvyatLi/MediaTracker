package ua.lsi.media_tracker.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by LSI on 03.03.2017.
 *
 * @author LSI
 */
public class Version {
    private static String version;

    public static String getVersion() {
        if (version == null) {
            version = loadVersion();
        }
        return version;
    }

    private static String loadVersion() {
        Properties properties = new Properties();
        try (InputStream inputStream = Version.class.getClassLoader().getResourceAsStream("version.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("version");
    }
}
