package ua.lsi.media_tracker.creators;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.StorageType;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

import static ua.lsi.media_tracker.enums.MessageCode.SETTINGS_NOT_SAVED;
import static ua.lsi.media_tracker.enums.MessageCode.SETTINGS_SAVED;
import static ua.lsi.media_tracker.enums.SettingsKey.AUTOMATIC_LOAD_ENABLED;
import static ua.lsi.media_tracker.enums.SettingsKey.DEFAULT_INFO_FILE;
import static ua.lsi.media_tracker.enums.SettingsKey.STORAGE_TYPE;

/**
 * Created by LSI on 29.03.2016.
 *
 * @author LSI
 */
@Component
public class Settings {
    private static final String PROPERTIES_FILE_NAME = "./MediaTracker.properties";
    private static Logger LOG = Logger.getLogger(Settings.class);
    private Boolean automaticLoadEnabled;
    private Properties properties;
    private File defaultInfoFile;
    private File settingsFile;
    private StorageType storageType;
    private Messages messages;

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public String saveSettings() {
        String resultMessage = messages.getMessage(SETTINGS_SAVED);
        properties.setProperty(AUTOMATIC_LOAD_ENABLED.name(), automaticLoadEnabled.toString());
        String defaultFileAbsolutePath = "";
        if (defaultInfoFile != null && defaultInfoFile.exists()) {
            defaultFileAbsolutePath = defaultInfoFile.getAbsolutePath();
        }
        properties.setProperty(DEFAULT_INFO_FILE.name(), defaultFileAbsolutePath);
        properties.setProperty(STORAGE_TYPE.name(), storageType.name());
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
                resultMessage = messages.getMessage(SETTINGS_NOT_SAVED);
            }
        } catch (IOException e) {
            LOG.error(e);
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

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    @PostConstruct
    public void init() {
        try {
            properties = new Properties();
            settingsFile = new File(PROPERTIES_FILE_NAME);
            boolean fileExists = true;
            if (!settingsFile.exists()) {
                fileExists = settingsFile.createNewFile();
            }
            if (fileExists) {
                try (InputStream inputStream = new FileInputStream(settingsFile)) {
                    properties.load(inputStream);
                    automaticLoadEnabled = Boolean.parseBoolean(properties.getProperty(AUTOMATIC_LOAD_ENABLED.name()));
                    String defaultInfoFilePath = properties.getProperty(DEFAULT_INFO_FILE.name());
                    if (defaultInfoFilePath != null) {
                        defaultInfoFile = new File(defaultInfoFilePath);
                    }
                    String storageTypeInSettings = properties.getProperty(STORAGE_TYPE.name());
                    if (storageTypeInSettings != null) {
                        storageType = StorageType.valueOf(storageTypeInSettings);
                    } else {
                        storageType = StorageType.FILE;
                    }
                }
            }
        } catch (IOException e) {
            LOG.error(e);
            throw new IllegalStateException("Error initializing settings" + e.getMessage());
        }
    }
}
