package ua.lsi.media_tracker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Messages;
import ua.lsi.media_tracker.model.Settings;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by LSI on 29.03.2016.
 *
 * @author LSI
 */
public class SettingsDialogController implements Initializable {

    @FXML
    CheckBox automaticLoadEnabled;
    @FXML
    Button closeButton;
    @FXML
    Label defaultFilePath;
    @FXML
    Label settingsSavedLabel;

    private Settings settings;
    private File defaultFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settings = Settings.getInstance();
        defaultFile = settings.getDefaultInfoFile();
        if (defaultFile != null && defaultFile.exists()) {
            defaultFilePath.setText(defaultFile.getAbsolutePath());
        }
        automaticLoadEnabled.setSelected(settings.isAutomaticLoadEnabled());
    }

    @FXML
    public void saveSettings() {
        settings.setAutomaticLoadEnabled(automaticLoadEnabled.isSelected());
        settings.setDefaultInfoFile(defaultFile);
        settings.saveSettings();
        if (defaultFile == null || !defaultFile.exists()) {
            automaticLoadEnabled.setSelected(false);
        }
        settingsSavedLabel.setText(Messages.getInstance().getMessage(MessageCode.SETTINGS_SAVED));
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
