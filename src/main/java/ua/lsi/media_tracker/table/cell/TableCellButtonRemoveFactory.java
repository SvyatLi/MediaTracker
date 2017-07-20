package ua.lsi.media_tracker.table.cell;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.controllers.MediaTrackerController;
import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 11.04.2016.
 *
 * @author LSI
 */
public class TableCellButtonRemoveFactory<S extends Media, T> extends AbstractTableCellButtonFactory<S, T> {

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
                        String section = getTableView().getId();
                        Media media = getTableView().getItems().get(getIndex());
                        MediaTrackerController controller = SpringFXMLLoader.getBeanFromContext(MediaTrackerController.class);
                        ButtonType removeButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButtonType = new ButtonType("Don't remove", ButtonBar.ButtonData.CANCEL_CLOSE);
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.getDialogPane().setContentText("Remove item ?");
                        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, cancelButtonType);
                        dialog.showAndWait().filter(response -> response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                                .ifPresent(response -> {
                                    controller.setModified(true);
                                    controller.removeItem(section, media);
                                });

                        getTableView().getColumns().get(0).setVisible(false);
                        getTableView().getColumns().get(0).setVisible(true);
                    });
                    setDefaultButtonParameters(btn, "removeButton");
                    setDefaultCellParameters(this, btn);
                }
            }
        };
        return cell;
    }
}
