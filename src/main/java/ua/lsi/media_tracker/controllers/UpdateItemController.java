package ua.lsi.media_tracker.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by LSI on 17.04.2016.
 *
 * @author LSI
 */
@Component
@RequiredArgsConstructor
public class UpdateItemController extends AbstractController implements Initializable {
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

    private Media media;
    private String oldSection;

    private final Messages messages;
    private final MediaTrackerController mediaTrackerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collection<String> sections = mediaTrackerController.getSections();
        if (sections.isEmpty()) {
            sections = Collections.singletonList(messages.getMessage(MessageCode.DEFAULT_SECTION));
        }
        sectionComboBox.setItems(FXCollections.observableArrayList(sections));
        sectionComboBox.getSelectionModel().selectFirst();
    }

    public void setValuesFromMedia(String currentSection, Media media) {
        sectionComboBox.getSelectionModel().select(currentSection);
        this.oldSection = currentSection;
        nameTextField.setText(media.getName());
        seasonTextField.setText(Integer.toString(media.getSeason()));
        episodeTextField.setText(Integer.toString(media.getEpisode()));
        this.media = media;
    }

    @FXML
    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void updateItem() {
        String section = sectionComboBox.getValue();
        Media media = updateMediaFromTextFields();
        if (!oldSection.equals(section)) {
            mediaTrackerController.moveItem(media, oldSection, section);
        }
        mediaTrackerController.setModified(true);

        close();
    }

    private Media updateMediaFromTextFields() {
        String name = nameTextField.getText();
        Integer season = getIntValueOrZeroFromTextInput(seasonTextField);
        seasonTextField.setText(season.toString());
        Integer episode = getIntValueOrZeroFromTextInput(episodeTextField);
        episodeTextField.setText(episode.toString());
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
}
