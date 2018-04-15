package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.www.content.audio.x_aiff;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.dao.MediaAccessProvider;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Service
@Log4j
public class MediaTrackerController extends AbstractController {
    @FXML
    Label statusLabel;

    @FXML
    VBox sectionsContainer;

    @Getter
    private Stage stage;

    @Autowired
    private MediaAccessProvider mediaAccessProvider;

    private Boolean modified = false;

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void autoLoad() {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() {
                return mediaAccessProvider.tryLoadFromSavedResource();
            }
        };
        task.setOnSucceeded(event -> {
            setupStatusLabelWithText(task.getValue());
            createAndShowTableViews(mediaAccessProvider.getSectionToMediaMap());
        });
        task.setOnFailed(event -> log.error(event.getSource().getException().getMessage(), event.getSource().getException()));
        new Thread(task).start();
    }

    @FXML
    public void loadData() {
        String statusMessage = mediaAccessProvider.loadInformation();
        setupStatusLabelWithText(statusMessage);
        createAndShowTableViews(mediaAccessProvider.getSectionToMediaMap());
    }

    public void loadDataFromDraggedFile(File file) {
        String statusMessage = mediaAccessProvider.loadInformationFromFile(file);
        setupStatusLabelWithText(statusMessage);
        createAndShowTableViews(mediaAccessProvider.getSectionToMediaMap());
    }

    @FXML
    public void saveData() {
        String statusMessage = mediaAccessProvider.saveMediaMap(SaveType.AUTOMATIC);
        setupStatusLabelWithText(statusMessage);
    }

    @FXML
    public void saveDataWithDialog() {
        String statusMessage = mediaAccessProvider.saveMediaMap(SaveType.MANUAL);
        setupStatusLabelWithText(statusMessage);
    }

    public void addNewItem(String section, Media media) {

        Map<String, List<Media>> mediaMap = mediaAccessProvider.getSectionToMediaMap();
        if (mediaMap.isEmpty()) {
            sectionsContainer.getChildren().clear();
        }
        List<Media> mediaList;
        if (mediaMap.containsKey(section)) {
            mediaList = mediaMap.get(section);
            mediaList.add(media);
        } else {
            mediaList = FXCollections.observableArrayList();
            if (media != null) {
                mediaList.add(media);
            }
            mediaMap.put(section, mediaList);
            addLabelAndTableViewToVBox(section, mediaList);
        }
    }

    public void moveItem(Media media, String oldSection, String newSection) {
        Map<String, List<Media>> mediaMap = mediaAccessProvider.getSectionToMediaMap();
        List<Media> mediaList = mediaMap.get(oldSection);
        mediaList.remove(media);
        List<Media> mediaSecondList = mediaMap.get(newSection);
        mediaSecondList.add(media);
        setupStatusLabelWithText("Item \"" + media.getName() + "\" moved. You need to save changes");
    }

    public void removeItem(String section, Media media) {
        Map<String, List<Media>> mediaMap = mediaAccessProvider.getSectionToMediaMap();
        MediaTrackerController controller = SpringFXMLLoader.getBeanFromContext(MediaTrackerController.class);
        List<Media> mediaList = mediaMap.get(section);
        mediaList.remove(media);
        mediaAccessProvider.removeMedia(media);
        controller.setModified(false);
        setupStatusLabelWithText("Item \"" + media.getName() + "\" removed. You need to save changes");
    }

    public void removeSection(String section) {
        Map<String, List<Media>> mediaMap = mediaAccessProvider.getSectionToMediaMap();
        MediaTrackerController controller = SpringFXMLLoader.getBeanFromContext(MediaTrackerController.class);
        mediaMap.remove(section);
        mediaAccessProvider.removeSection(section);
        sectionsContainer.getChildren()
                .removeAll(sectionsContainer.getChildren().stream()
                        .filter(x -> x.getId().equals(section))
                        .collect(Collectors.toList()));

        controller.setModified(false);
        setupStatusLabelWithText("Section \"" + section + "\" removed. You need to save changes");
    }

    private void createAndShowTableViews(Map<String, List<Media>> mediaMap) {
        if (!mediaMap.isEmpty()) {
            sectionsContainer.getChildren().clear();
        }
        for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
            addLabelAndTableViewToVBox(entry.getKey(), entry.getValue());
        }
    }

    private void addLabelAndTableViewToVBox(String section, List<Media> mediaList) {
        Node table = createTable(section, mediaList);
        sectionsContainer.getChildren().add(table);
    }

    private Node createTable(String section, List<Media> list) {
        MediaTableController mediaTableController = (MediaTableController) SpringFXMLLoader.load("/view/table_template.fxml");
        return mediaTableController.setup(section, list);
    }

    @FXML
    public void openSettings() {
        SettingsDialogController settingsDialogController = (SettingsDialogController) SpringFXMLLoader.load("/view/settings_dialog.fxml");
        Scene scene = new Scene((Parent) settingsDialogController.getView());
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Settings");
        dialog.initOwner(stage);
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    public void openAbout() {
        AboutController aboutController = (AboutController) SpringFXMLLoader.load("/view/about.fxml");
        Scene scene = new Scene((Parent) aboutController.getView());
        final Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.setScene(scene);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.show();
        dialog.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dialog.close();
            }
        });
    }

    @FXML
    public void openAddItemDialog() {
        AddItemController addItemController = (AddItemController) SpringFXMLLoader.load("/view/add_item.fxml");
        Scene scene = new Scene((Parent) addItemController.getView());
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.setTitle("Add Item");
        dialog.initOwner(stage);
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    public void openAddSectionDialog() {
        AddSectionController addItemController = (AddSectionController) SpringFXMLLoader.load("/view/add_section.fxml");
        Scene scene = new Scene((Parent) addItemController.getView());
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.setTitle("Add Section");
        dialog.initOwner(stage);
        dialog.setScene(scene);
        dialog.show();
    }

    private void setupStatusLabelWithText(String text) {
        statusLabel.setText(text);
        statusLabel.setTooltip(new Tooltip(text));
        clearLabelAfterDelay(statusLabel, 5000);
    }

    public Set<String> getSections() {
        return mediaAccessProvider.getSectionToMediaMap().keySet();
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public void promptSaveOnClose() {
        if (modified) {
            ButtonType removeButtonType = new ButtonType("Save and exit", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContentText("Save changes ?");
            dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, cancelButtonType);
            dialog.showAndWait().filter(response -> response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    .ifPresent(response -> saveData());
        }
    }
}
