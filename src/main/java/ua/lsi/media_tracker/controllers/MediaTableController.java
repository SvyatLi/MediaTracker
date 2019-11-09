package ua.lsi.media_tracker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.model.Media;

import java.util.List;
import java.util.Optional;

/**
 * Created by LSI on 17.04.2016.
 *
 * @author LSI
 */
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class MediaTableController extends AbstractController {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    @FXML
    Label sectionName;

    @FXML
    TableView<Media> mediaTableView;

    @FXML
    VBox parentBox;

    private final  MediaTrackerController mediaTrackerController;

    public Node setup(String section, List<Media> list) {
        mediaTableView.setId(section);
        sectionName.setText(section);
        mediaTableView.setItems((ObservableList<Media>) list);
        mediaTableView.setRowFactory(tv -> {
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
                    Media draggedPerson = mediaTableView.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = mediaTableView.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    mediaTableView.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    mediaTableView.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });
        setupHeightAndWidthForTable(mediaTableView);
        return parentBox;
    }

    private void setupHeightAndWidthForTable(TableView<Media> table) {
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(Bindings.size(table.getItems())
                        .add(2.0)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    @FXML
    public void addItemToSection() {
        AddItemController addItemController = (AddItemController) SpringFXMLLoader.load("/view/add_item.fxml");
        addItemController.selectSection(sectionName.getText());
        Scene scene = new Scene((Parent) addItemController.getView());
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.setTitle("Add Item");
        dialog.initOwner(mediaTrackerController.getStage());
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    public void removeSection() {
        mediaTrackerController.removeSection(sectionName.getText());
    }
}
