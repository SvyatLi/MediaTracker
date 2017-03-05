package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Service
@Log4j
public class MediaTrackerController extends AbstractController {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    @FXML
    Label statusLabel;
    @Getter
    private Stage stage;

    @Autowired
    private Settings settings;

    @Autowired
    private MediaAccessProvider mediaAccessProvider;

    private Boolean modified = false;

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void autoLoad() {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return mediaAccessProvider.tryLoadFromSavedResource();
            }
        };
        task.setOnSucceeded(event -> {
            setupStatusLabelWithText(task.getValue());
            createAndShowTableViews(mediaAccessProvider.getSectionToMediaMap());
        });
        task.setOnFailed(event -> log.error(event.getSource().getException()));
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
        List<Media> mediaList;
        if (mediaMap.containsKey(section)) {
            mediaList = mediaMap.get(section);
            mediaList.add(media);
        } else {
            mediaList = FXCollections.observableArrayList(media);
            mediaMap.put(section, mediaList);
            addLabelAndTableViewToVBox(section, mediaList);
        }
    }

    public void removeItem(String section, Media media) {
        Map<String, List<Media>> mediaMap = mediaAccessProvider.getSectionToMediaMap();
        List<Media> mediaList = mediaMap.get(section);
        mediaList.remove(media);
        setupStatusLabelWithText("Item \"" + media.getName() + "\" removed. You need to save changes");
    }

    private void createAndShowTableViews(Map<String, List<Media>> mediaMap) {
        VBox box = getVBoxFromStage(stage);

        box.getChildren().clear();
        for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
            addLabelAndTableViewToVBox(entry.getKey(), entry.getValue());
        }
    }

    private void addLabelAndTableViewToVBox(String section, List<Media> mediaList) {
        VBox box = getVBoxFromStage(stage);

        Label label = new Label(section);
        label.setFont(Font.font(24));
        box.getChildren().add(label);
        TableView<Media> table = createTable(mediaList);
        table.setId(section);
        box.getChildren().add(table);
    }

    private TableView<Media> createTable(List<Media> list) {
        TableView<Media> table = SpringFXMLLoader.loadNode("/view/table_template.fxml");
        table.setItems((ObservableList<Media>) list);
        table.setRowFactory(tv -> {
            TableRow<Media> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Media draggedPerson = table.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = table.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    table.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    table.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });
        setupHeightAndWidthForTable(table);
        return table;
    }

    private void setupHeightAndWidthForTable(TableView<Media> table) {
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(Bindings.size(table.getItems()).add(2.0)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    private VBox getVBoxFromStage(Stage stage) {
        return (VBox) stage.getScene().lookup("#scrollVBox");
    }

    @FXML
    public void openSettings() throws IOException {
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

    private void setupStatusLabelWithText(String text) {
        statusLabel.setText(text);
        statusLabel.setTooltip(new Tooltip(text));
        clearLabelAfterDelay(statusLabel, 5000);
    }

//    private MediaContainer getMediaContainer() {
//        return objectProvider.getMediaContainer(settings.getStorageType());
//    }

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
