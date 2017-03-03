package ua.lsi.media_tracker.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.ObjectProvider;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;

import java.net.URL;
import java.util.*;

/**
 * Created by LSI on 17.04.2016.
 *
 * @author LSI
 */
@Component
public class AddItemController extends AbstractController implements Initializable {
    @FXML
    public Button closeButton;
    @FXML
    public ComboBox<String> sectionComboBox;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField seasonTextField;
    @FXML
    public TextField episodeTextField;
    @FXML
    public Label addingStatusLabel;
    @FXML
    public TextField sectionTextField;

    private ObjectProvider objectProvider;
    private Settings settings;
    private Messages messages;
    private MediaTrackerController mediaTrackerController;

    @Autowired
    public void setObjectProvider(ObjectProvider objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Autowired
    public void setMediaTrackerController(MediaTrackerController mediaTrackerController) {
        this.mediaTrackerController = mediaTrackerController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collection<String> sections = mediaTrackerController.getSections();
        if (sections.isEmpty()) {
            sections = Collections.singletonList(messages.getMessage(MessageCode.DEFAULT_SECTION));
        }
        sectionComboBox.setItems(FXCollections.observableArrayList(sections));
        sectionComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addItem() {
        String section = sectionComboBox.getValue();
        Media media = createMediaFromTextFields();
        mediaTrackerController.addNewItem(section, media);
        addingStatusLabel.setText("Item Added");
        clearLabelAfterDelay(addingStatusLabel, 2000);
    }

    private Media createMediaFromTextFields() {
        String name = nameTextField.getText();
        Integer season = getIntValueOrZeroFromTextInput(seasonTextField);
        seasonTextField.setText(season.toString());
        Integer episode = getIntValueOrZeroFromTextInput(episodeTextField);
        episodeTextField.setText(episode.toString());
        Media media = new Media();
        media.setName(name);
        media.setSeason(season);
        media.setEpisode(episode);
        return media;
    }

    private Integer getIntValueOrZeroFromTextInput(TextField textField) {
        if (textField != null) {
            String value = textField.getText();
            if (value != null && !value.isEmpty() && value.matches("[0-9]*")) {
                return Integer.parseInt(value, 10);
            }
        }
        return 0;
    }

    @FXML
    public void addSection() {
        String newSection = sectionTextField.getText();
        if (newSection != null && !newSection.isEmpty()) {
            sectionComboBox.getItems().add(newSection);
            sectionTextField.clear();
            addingStatusLabel.setText("Section Added");
        } else {
            addingStatusLabel.setText("Wrong section name");
        }
        clearLabelAfterDelay(addingStatusLabel, 2000);
    }
}
