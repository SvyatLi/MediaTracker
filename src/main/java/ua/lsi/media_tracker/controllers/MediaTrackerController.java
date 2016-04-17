package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
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
        MediaContainer container = objectProvider.getMediaContainer(settings.getStorageType());
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return container.tryLoadFromSavedResource();
            }
        };
        task.setOnSucceeded(event -> {
            setupStatusLabelWithText(task.getValue());
            createView(container);
        });
        task.setOnFailed(event -> LOG.error(event.getSource().getException()));
        new Thread(task).start();
    }

    @FXML
    public void loadData() {
        MediaContainer container = objectProvider.getMediaContainer(settings.getStorageType());
        String statusMessage = container.loadInformation();
        setupStatusLabelWithText(statusMessage);
        createView(container);
    }

    public void loadDataFromDraggedFile(File file) {
        MediaContainer container = objectProvider.getMediaContainer(StorageType.FILE);
        String statusMessage = container.loadInformationFromFile(file);
        setupStatusLabelWithText(statusMessage);
        createView(container);
    }

    @FXML
    public void saveData() {
        MediaContainer container = objectProvider.getMediaContainer(settings.getStorageType());
        String statusMessage = container.saveMediaMap();
        setupStatusLabelWithText(statusMessage);
    }

    private void createView(MediaContainer container) {
        Scene scene = stage.getScene();
        VBox box = (VBox) scene.lookup("#scrollVBox");

        box.getChildren().clear();
        Map<String, List<Media>> mediaMap = container.getSectionToMediaMap();
        for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
            Label label = new Label(entry.getKey());
            label.setFont(Font.font(24));
            box.getChildren().add(label);
            TableView<Media> table = createTable(entry.getValue());
            box.getChildren().add(table);
        }
    }

    private TableView<Media> createTable(List<Media> list) {
        TableView<Media> table = (TableView<Media>) SpringFXMLLoader.loadNode("view/table_template.fxml");
        table.setItems((ObservableList<Media>) list);
        setupHeightAndWidthForTable(table);
        return table;
    }

    private void setupHeightAndWidthForTable(TableView<Media> table) {
        table.setEditable(true);
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(Bindings.size(table.getItems()).add(2.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

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
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Item");
        dialog.initOwner(stage);
        dialog.setScene(scene);
        dialog.show();
    }

    private void setupStatusLabelWithText(String text) {
        statusLabel.setText(text);
        statusLabel.setTooltip(new Tooltip(text));
    }
}
