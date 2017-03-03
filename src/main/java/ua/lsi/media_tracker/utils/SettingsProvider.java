package ua.lsi.media_tracker.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;
/**
 * @author Sviatoslav Likhota
 *         on 3/3/17.
 */
@Component
public class SettingsProvider {

    private static final String FILE_NAME = "settings.properties";

    private static String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".mediatracker" + File.separator;
    }

    private Properties properties = new Properties();

    public SettingsProvider addProperty(String key, String value) {
        properties.setProperty(key, value);
        return this;
    }

    public void save() throws IOException {
        try {
            File propsDir = new File(getUserDataDirectory());
            propsDir.mkdir();
            File propsFile = new File(getUserDataDirectory() + FILE_NAME);
            propsFile.createNewFile();

            try (OutputStream outputStream = new FileOutputStream(propsFile)) {
                properties.store(outputStream, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public boolean propertiesExist() {
        File propsFile = new File(getUserDataDirectory() + FILE_NAME);
        if (propsFile.exists()) {
            try (InputStream inputStream = new FileInputStream(propsFile)) {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
