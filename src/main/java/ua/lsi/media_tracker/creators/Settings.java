package ua.lsi.media_tracker.creators;

import lombok.Data;
import lombok.extern.log4j.Log4j;
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
@Data
@Log4j
public class Settings {
    private Boolean automaticLoadEnabled;
    private File defaultInfoFile;
    private StorageType storageType;
    @Autowired
    private Messages messages;

    @Autowired
    private SettingsProvider sp;

    public String saveSettings() {
        String resultMessage = messages.getMessage(SETTINGS_SAVED);

        String defaultFileAbsolutePath = "";
        if (defaultInfoFile != null && defaultInfoFile.exists()) {
            defaultFileAbsolutePath = defaultInfoFile.getAbsolutePath();
        }

        try {
            sp.addProperty(AUTOMATIC_LOAD_ENABLED.name(), automaticLoadEnabled.toString())
                    .addProperty(DEFAULT_INFO_FILE.name(), defaultFileAbsolutePath)
                    .addProperty(STORAGE_TYPE.name(), StorageType.SQLITE.name())
                    .save();
        } catch (IOException e) {
            log.error(e);
            resultMessage = messages.getMessage(SETTINGS_NOT_SAVED);
        }

        return resultMessage;
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
                storageType = StorageType.SQLITE;
            } else {
                storageType = StorageType.SQLITE;
            }
        } else {
            log.info("Setting default values");
            storageType = StorageType.SQLITE;
            automaticLoadEnabled = false;
        }
    }

    public File getDefaultSaveFile() {
        defaultInfoFile = sp.getDefaultSaveFile();
        automaticLoadEnabled = true;
        saveSettings();
        return defaultInfoFile;
    }
}
