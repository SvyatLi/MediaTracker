package ua.lsi.media_tracker.creators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

import static ua.lsi.media_tracker.enums.MessageCode.PROPERTIES_FILE;
import static ua.lsi.media_tracker.enums.MessageCode.SETTINGS_NOT_SAVED;
import static ua.lsi.media_tracker.enums.MessageCode.SETTINGS_SAVED;
import static ua.lsi.media_tracker.enums.SettingsKey.*;

/**
 * Created by LSI on 29.03.2016.
 *
 * @author LSI
 */
@Component
public class Settings {
    private static Settings settings;

    private Boolean automaticLoadEnabled;
    private Properties properties;
    private File defaultInfoFile;
    private File settingsFile;

    private Settings() {
        properties = new Properties();
    }

    public static Settings getInstance() {
        return settings;
    }

    public String saveSettings() {
        String resultMessage = Messages.getInstance().getMessage(SETTINGS_SAVED);
        properties.setProperty(AUTOMATIC_LOAD_ENABLED.name(), automaticLoadEnabled.toString());
        String defaultFileAbsolutePath = "";
        if (defaultInfoFile != null && defaultInfoFile.exists()) {
            defaultFileAbsolutePath = defaultInfoFile.getAbsolutePath();
        }
        properties.setProperty(DEFAULT_INFO_FILE.name(), defaultFileAbsolutePath);
        try {
            boolean fileExists = true;
            if (!settingsFile.exists()) {
                fileExists = settingsFile.createNewFile();
            }
            if (fileExists) {
                try (OutputStream outputStream = new FileOutputStream(settingsFile)) {
                    properties.store(outputStream, null);
                }
            } else {
                resultMessage = Messages.getInstance().getMessage(SETTINGS_NOT_SAVED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultMessage;
    }

    public File getDefaultInfoFile() {
        return defaultInfoFile;
    }

    public void setDefaultInfoFile(File defaultInfoFile) {
        this.defaultInfoFile = defaultInfoFile;
    }

    public boolean isAutomaticLoadEnabled() {
        return automaticLoadEnabled;
    }

    public void setAutomaticLoadEnabled(Boolean automaticLoadEnabled) {
        this.automaticLoadEnabled = automaticLoadEnabled;
    }

    public static class Builder {
        private Settings instance;

        public Builder() {
            instance = new Settings();
        }

        public Settings build() {
            String fileName = Messages.getInstance().getMessage(PROPERTIES_FILE);
            String tempFolderPath = System.getProperty("java.io.tmpdir");
            try {
                instance.settingsFile = new File(tempFolderPath + fileName);
                boolean fileExists = true;
                if (!instance.settingsFile.exists()) {
                    fileExists = instance.settingsFile.createNewFile();
                }
                if (fileExists) {
                    try (InputStream inputStream = new FileInputStream(instance.settingsFile)) {
                        instance.properties.load(inputStream);
                        instance.automaticLoadEnabled = Boolean.parseBoolean(instance.properties.getProperty(AUTOMATIC_LOAD_ENABLED.name()));
                        String defaultInfoFilePath = instance.properties.getProperty(DEFAULT_INFO_FILE.name());
                        if (defaultInfoFilePath != null) {
                            instance.defaultInfoFile = new File(defaultInfoFilePath);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException("Error initializing settings" + e.getMessage());
            }
            return instance;
        }
    }

    @Autowired
    public void setSettings(Settings settings) {
        Settings.settings = settings;
    }

}
