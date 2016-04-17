package ua.lsi.media_tracker.controllers;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.creators.ObjectProvider;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.model.Media;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by LSI on 17.04.2016.
 *
 * @author LSI
 */
@Component
public class AddItemController extends AbstractController implements Initializable {
    private static Logger LOG = Logger.getLogger(AddItemController.class);

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

    private Map<String, List<Media>> mediaMap;
    private ObjectProvider objectProvider;
    private Settings settings;

    @Autowired
    public void setObjectProvider(ObjectProvider objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaContainer container = objectProvider.getMediaContainer(settings.getStorageType());
        mediaMap = container.getSectionToMediaMap();
        sectionComboBox.setItems(FXCollections.observableArrayList(mediaMap.keySet()));
        sectionComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addItem(ActionEvent actionEvent) {
        String section = sectionComboBox.getValue();
        Media media = createMediaFromTextInputs();
        //TODO: find a way to add new table
        List<Media> mediaList = mediaMap.getOrDefault(section, new ArrayList<>());
        mediaList.add(media);
        addingStatusLabel.setText("Item Added");
        clearLabelAfterDelay(2000);
    }

    private Media createMediaFromTextInputs(){
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

    private void clearLabelAfterDelay(final int millis){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(millis);
                return null;
            }
        };
        task.setOnSucceeded(event -> addingStatusLabel.setText(""));
        task.setOnFailed(event -> LOG.error(event.getSource().getException()));
        new Thread(task).start();
    }
}
