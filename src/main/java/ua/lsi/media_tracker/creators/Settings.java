package ua.lsi.media_tracker.creators;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.StorageType;
import ua.lsi.media_tracker.utils.SettingsProvider;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

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
    private static Logger LOG = Logger.getLogger(Settings.class);
    private Boolean automaticLoadEnabled;
    private File defaultInfoFile;
    private StorageType storageType;
    private Messages messages;

    @Autowired
    private SettingsProvider sp;

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public String saveSettings() {
        String resultMessage = messages.getMessage(SETTINGS_SAVED);

        String defaultFileAbsolutePath = "";
        if (defaultInfoFile != null && defaultInfoFile.exists()) {
            defaultFileAbsolutePath = defaultInfoFile.getAbsolutePath();
        }

        try {
            sp.addProperty(AUTOMATIC_LOAD_ENABLED.name(), automaticLoadEnabled.toString())
                    .addProperty(DEFAULT_INFO_FILE.name(), defaultFileAbsolutePath)
                    .addProperty(STORAGE_TYPE.name(), storageType.name())
                    .save();
        } catch (IOException e) {
            LOG.error(e);
            resultMessage = messages.getMessage(SETTINGS_NOT_SAVED);
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
        if (sp.propertiesExist()) {
            automaticLoadEnabled = Boolean.parseBoolean(sp.getProperty(AUTOMATIC_LOAD_ENABLED.name(), "False"));
            String defaultInfoFilePath = sp.getProperty(DEFAULT_INFO_FILE.name(), null);
            if (defaultInfoFilePath != null) {
                defaultInfoFile = new File(defaultInfoFilePath);
            }
            String storageTypeInSettings = sp.getProperty(STORAGE_TYPE.name(), null);
            if (storageTypeInSettings != null) {
                storageType = StorageType.valueOf(storageTypeInSettings);
            } else {
                storageType = StorageType.FILE;
            }
        } else {
            storageType = StorageType.FILE;
            automaticLoadEnabled = false;
        }
    }
}
