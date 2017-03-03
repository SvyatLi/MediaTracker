package ua.lsi.media_tracker.table.cell;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.controllers.MediaTrackerController;
import ua.lsi.media_tracker.controllers.UpdateItemController;
import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 25.04.2016.
 *
 * @author LSI
 */
public class TableCellButtonEditFactory<S extends Media, T> extends AbstractTableCellButtonFactory<S, T> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        final TableCell<S, T> cell = new TableCell<S, T>() {
            final Button btn = new Button("");

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction((ActionEvent event) ->
                    {
                        String currentSection = getTableView().getId();
                        Media media = getTableView().getItems().get(getIndex());
                        MediaTrackerController controller = SpringFXMLLoader.getBeanFromContext(MediaTrackerController.class);

                        UpdateItemController updateItemController = (UpdateItemController) SpringFXMLLoader.load("/view/update_item.fxml");
                        Scene scene = new Scene((Parent) updateItemController.getView());
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.WINDOW_MODAL);
                        dialog.setTitle("Change Item");
                        dialog.initOwner(controller.getStage());
                        dialog.setScene(scene);
                        dialog.show();

                        updateItemController.setValuesFromMedia(currentSection, media);

                        getTableView().getColumns().get(0).setVisible(false);
                        getTableView().getColumns().get(0).setVisible(true);
                    });
                    setDefaultButtonParameters(btn, "editButton");
                    setDefaultCellParameters(this, btn);
                }
            }
        };
        return cell;
    }
}
