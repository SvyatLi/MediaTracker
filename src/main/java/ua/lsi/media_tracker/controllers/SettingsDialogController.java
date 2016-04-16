package ua.lsi.media_tracker.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.enums.StorageType;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by LSI on 29.03.2016.
 *
 * @author LSI
 */
@Component
public class SettingsDialogController extends AbstractController implements Initializable {

    private static Logger LOG = Logger.getLogger(SettingsDialogController.class);

    @FXML
    public CheckBox automaticLoadEnabled;
    @FXML
    public Button closeButton;
    @FXML
    public Label defaultFilePath;
    @FXML
    public Label settingsSavedLabel;
    @FXML
    public ComboBox<StorageType> storageTypeComboBox;

    private Settings settings;
    private Messages messages;
    private File defaultFile;

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultFile = settings.getDefaultInfoFile();
        if (defaultFile != null && defaultFile.exists()) {
            defaultFilePath.setText(defaultFile.getAbsolutePath());
        }
        automaticLoadEnabled.setSelected(settings.isAutomaticLoadEnabled());
        storageTypeComboBox.setItems(FXCollections.observableArrayList(StorageType.values()));
        storageTypeComboBox.getSelectionModel().select(settings.getStorageType());
    }

    @FXML
    public void saveSettings() {
        settings.setAutomaticLoadEnabled(automaticLoadEnabled.isSelected());
        settings.setDefaultInfoFile(defaultFile);
        settings.setStorageType(storageTypeComboBox.getValue());
        settings.saveSettings();
        if (defaultFile == null || !defaultFile.exists()) {
            automaticLoadEnabled.setSelected(false);
        }
        settingsSavedLabel.setText(messages.getMessage(MessageCode.SETTINGS_SAVED));
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        defaultFile = fileChooser.showOpenDialog(null);
        String defaultFileAbsolutePath = "File not set";
        if (defaultFile != null && defaultFile.exists()) {
            defaultFileAbsolutePath = defaultFile.getAbsolutePath();
        }
        defaultFilePath.setText(defaultFileAbsolutePath);
    }
}
