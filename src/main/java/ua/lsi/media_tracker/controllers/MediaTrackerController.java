package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.creators.ObjectProvider;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.enums.StorageType;
import ua.lsi.media_tracker.model.Media;

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

    @Autowired
    public void setObjectProvider(ObjectProvider objectProvider) {
        this.objectProvider = objectProvider;
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void autoLoad() {
        MediaContainer container = ObjectProvider.getMediaContainer(StorageType.FILE);
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return container.tryLoadFromSavedResource();
            }
        };
        task.setOnSucceeded(event -> {
            statusLabel.setText(task.getValue());
            createView(container);
        });
        task.setOnFailed(event -> LOG.error(event.getSource().getException()));
        new Thread(task).start();
    }

    @FXML
    public void loadData() {
        MediaContainer container = ObjectProvider.getMediaContainer(StorageType.FILE);
        String statusMessage = container.loadInformation();
        statusLabel.setText(statusMessage);
        createView(container);
    }

    @FXML
    public void saveData() {
        MediaContainer container = ObjectProvider.getMediaContainer(StorageType.FILE);
        String statusMessage = container.saveMediaMap();
        statusLabel.setText(statusMessage);
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

    private void createView(MediaContainer container) {
        Scene scene = stage.getScene();
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);

        Map<String, List<Media>> mediaMap = container.getSectionToMediaMap();
        for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
            Label label = new Label(entry.getKey());
            label.setFont(Font.font(24));
            box.getChildren().add(label);
            TableView<Media> table = createTable(entry.getValue());
            box.getChildren().add(table);
        }
        scrollPane.setContent(box);
    }

    private TableView<Media> createTable(List<Media> list) {
        TableView<Media> table = new TableView<>();

        createAndAddToTableSimpleColumn(table, "Name");
        createAndAddToTableColumnWithMinusAndPlus(table, "Season");
        createAndAddToTableColumnWithMinusAndPlus(table, "Episode");

        table.getItems().setAll(list);

        setupHeightAndWidthForTable(table);

        return table;
    }

    private void createAndAddToTableSimpleColumn(TableView<Media> table, String columnName) {
        TableColumn<Media, String> name = new TableColumn<>(columnName);
        name.setMinWidth(250);
        name.setCellValueFactory(new PropertyValueFactory<>(columnName.toLowerCase()));
        table.getColumns().add(name);
    }

    private void createAndAddToTableColumnWithMinusAndPlus(TableView<Media> table, String columnName) {
        TableColumn<Media, String> wrapper = new TableColumn<>(columnName);
        wrapper.setMinWidth(100);

        TableColumn<Media, String> minus = new TableColumn<>("-");
        minus.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        minus.setCellFactory(getCallback());

        TableColumn<Media, String> number = new TableColumn<>("#");
        number.setCellValueFactory(new PropertyValueFactory<>(columnName.toLowerCase()));

        TableColumn<Media, String> plus = new TableColumn<>("+");
        plus.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        plus.setCellFactory(getCallback());

        wrapper.getColumns().add(minus);
        wrapper.getColumns().add(number);
        wrapper.getColumns().add(plus);
        table.getColumns().add(wrapper);
    }

    private void setupHeightAndWidthForTable(TableView<Media> table) {
        table.setEditable(true);
        Pane pane = (Pane) stage.getScene().getRoot();
        table.setPrefWidth(pane.getWidth() - 50);
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(Bindings.size(table.getItems()).add(2.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

    }

    private Callback<TableColumn<Media, String>, TableCell<Media, String>> getCallback() {
        return new Callback<TableColumn<Media, String>, TableCell<Media, String>>() {
            @Override
            public TableCell<Media, String> call(final TableColumn<Media, String> param) {
                final TableCell<Media, String> cell = new TableCell<Media, String>() {
                    final Button btn = new Button("");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction((ActionEvent event) ->
                            {
                                Media media = getTableView().getItems().get(getIndex());
                                String columnName = getTableColumn().getParentColumn().getText();
                                String sign = getTableColumn().getText();
                                media.change(columnName, sign);
                                getTableView().getColumns().get(0).setVisible(false);
                                getTableView().getColumns().get(0).setVisible(true);
                            });
                            btn.setText(getTableColumn().getText());
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
    }
}
