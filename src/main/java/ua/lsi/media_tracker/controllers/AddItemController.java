package ua.lsi.media_tracker.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
public class AddItemController extends AbstractController implements Initializable {
    @FXML
    public Button closeButton;
    @FXML
    public ComboBox<String> sectionComboBox;
    @FXML
    public TextField nameTextField;
    @FXML
    public Spinner<Integer> seasonSpinner;
    @FXML
    public Spinner<Integer> episodeSpinner;
    @FXML
    public Label addingStatusLabel;

    @Autowired
    private Messages messages;

    @Autowired
    private MediaTrackerController mediaTrackerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collection<String> sections = mediaTrackerController.getSections();
        if (sections.isEmpty()) {
            sections = Collections.singletonList(messages.getMessage(MessageCode.DEFAULT_SECTION));
        }
        sectionComboBox.setItems(FXCollections.observableArrayList(sections));
        sectionComboBox.getSelectionModel().selectFirst();
        seasonSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        seasonSpinner.setEditable(true);
        episodeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        episodeSpinner.setEditable(true);
    }

    @FXML
    public void close() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void addItem() {
        String section = sectionComboBox.getValue();
        Media media = createMediaFromTextFields();
        mediaTrackerController.addNewItem(section, media);
        addingStatusLabel.setText("Item Added");
        clearLabelAfterDelay(addingStatusLabel, 2000);
        close();
    }

    public void selectSection(String section) {
        sectionComboBox.getSelectionModel().select(section);
    }

    private Media createMediaFromTextFields() {
        String name = nameTextField.getText();
        Integer season = seasonSpinner.getValue();
        Integer episode = episodeSpinner.getValue();
        Media media = new Media();
        media.setName(name);
        media.setSeason(season);
        media.setEpisode(episode);
        return media;
    }
}
