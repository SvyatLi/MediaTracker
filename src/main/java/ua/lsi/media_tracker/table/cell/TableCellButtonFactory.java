package ua.lsi.media_tracker.table.cell;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ua.lsi.media_tracker.Main;
import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 11.04.2016.
 *
 * @author LSI
 */
public class TableCellButtonFactory<S extends Media, T> extends AbstractTableCellButtonFactory<S, T> {

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
                        Media media = getTableView().getItems().get(getIndex());
                        String columnName = getTableColumn().getParentColumn().getText();
                        String sign = getTableColumn().getText();
                        media.change(columnName, sign);
                        getTableView().getColumns().get(0).setVisible(false);
                        getTableView().getColumns().get(0).setVisible(true);
                        Main.mediaTrackerController.saveData();
                    });
                    btn.setText(getTableColumn().getText());
                    setDefaultButtonParameters(btn, null);
                    setDefaultCellParameters(this, btn);
                }
            }
        };
        return cell;
    }
}
