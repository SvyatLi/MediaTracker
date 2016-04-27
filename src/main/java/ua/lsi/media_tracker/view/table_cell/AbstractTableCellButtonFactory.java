package ua.lsi.media_tracker.view.table_cell;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;
import javafx.util.Callback;
import ua.lsi.media_tracker.model.Media;

/**
 * Created by LSI on 27.04.2016.
 *
 * @author LSI
 */
abstract class AbstractTableCellButtonFactory<S extends Media, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    void setDefaultButtonParameters(Button btn, String id) {
        btn.setId(id);
        btn.setPadding(Insets.EMPTY);
        btn.setMinSize(30, 30);
        btn.setFont(Font.font(20));
        btn.setAlignment(Pos.CENTER);
    }

    void setDefaultCellParameters(TableCell<S, T> cell, Button btn) {
        cell.setPadding(Insets.EMPTY);
        cell.setBorder(Border.EMPTY);
        cell.setGraphic(btn);
        cell.setText(null);
    }
}
