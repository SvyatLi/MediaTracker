package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.creators.ObjectProvider;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.enums.StorageType;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Service
public class MediaTrackerController extends AbstractController {
    private static Logger LOG = Logger.getLogger(MediaTrackerController.class);
    @FXML
    Label statusLabel;
    private Stage stage;
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

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void autoLoad() {
        MediaContainer container = getMediaContainer();
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return container.tryLoadFromSavedResource();
            }
        };
        task.setOnSucceeded(event -> {
            setupStatusLabelWithText(task.getValue());
            createAndShowTableViews(container.getSectionToMediaMap());
        });
        task.setOnFailed(event -> LOG.error(event.getSource().getException()));
        new Thread(task).start();
    }

    @FXML
    public void loadData() {
        MediaContainer container = getMediaContainer();
        String statusMessage = container.loadInformation();
        setupStatusLabelWithText(statusMessage);
        createAndShowTableViews(container.getSectionToMediaMap());
    }

    public void loadDataFromDraggedFile(File file) {
        MediaContainer container = objectProvider.getMediaContainer(StorageType.FILE);
        settings.setStorageType(StorageType.FILE);
        String statusMessage = container.loadInformationFromFile(file);
        setupStatusLabelWithText(statusMessage);
        createAndShowTableViews(container.getSectionToMediaMap());
    }

    @FXML
    public void saveData() {
        MediaContainer container = getMediaContainer();
        String statusMessage = container.saveMediaMap(SaveType.AUTOMATIC);
        setupStatusLabelWithText(statusMessage);
    }

    @FXML
    public void saveDataWithDialog() {
        MediaContainer container = getMediaContainer();
        String statusMessage = container.saveMediaMap(SaveType.MANUAL);
        setupStatusLabelWithText(statusMessage);
    }

    public void addNewItem(String section, Media media) {
        MediaContainer container = getMediaContainer();

        Map<String, List<Media>> mediaMap = container.getSectionToMediaMap();
        List<Media> mediaList = null;
        if (mediaMap.containsKey(section)) {
            mediaList = mediaMap.get(section);
            mediaList.add(media);
        } else {
            mediaList = FXCollections.observableArrayList(media);
            mediaMap.put(section, mediaList);
            addLabelAndTableViewToVBox(section, mediaList);
        }
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
        box.getChildren().add(table);
    }

    private TableView<Media> createTable(List<Media> list) {
        TableView<Media> table = SpringFXMLLoader.loadNode("view/table_template.fxml");
        table.setItems((ObservableList<Media>) list);
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
        SettingsDialogController settingsDialogController = (SettingsDialogController) SpringFXMLLoader.load("view/settings_dialog.fxml");
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
        Node node = SpringFXMLLoader.loadNode("view/about.fxml");
        Scene scene = new Scene((Parent) node);
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
    public void addItem() {
        AddItemController addItemController = (AddItemController) SpringFXMLLoader.load("view/add_item.fxml");
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

    private MediaContainer getMediaContainer() {
        return objectProvider.getMediaContainer(settings.getStorageType());
    }
}
