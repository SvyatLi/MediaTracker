package ua.lsi.media_tracker.view.table_cell;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ua.lsi.media_tracker.SpringFXMLLoader;
import ua.lsi.media_tracker.controllers.MediaTrackerController;
import ua.lsi.media_tracker.model.Media;

import java.util.Optional;

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

                        ChoiceDialog<String> dialog = new ChoiceDialog<>(currentSection, controller.getSections());
                        dialog.setTitle("Choose section");
                        dialog.setHeaderText("Select section to move media to:");

                        Optional<String> result = dialog.showAndWait();

                        result.ifPresent(newSection -> {
                            if (!currentSection.equals(newSection)) {
                                controller.removeItem(currentSection, media);
                                controller.addNewItem(newSection, media);
                            }
                        });

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
