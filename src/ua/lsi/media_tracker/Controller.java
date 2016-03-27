package ua.lsi.media_tracker;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.dao.ObjectProvider;
import ua.lsi.media_tracker.emuns.StorageType;
import ua.lsi.media_tracker.model.Media;

import java.util.List;
import java.util.Map;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Controller{

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadData() {
        MediaContainer container = ObjectProvider.getMediaContainer(StorageType.FILE);
        container.init();

        Scene scene = stage.getScene();
        Parent root = scene.getRoot();

        if (root instanceof Pane) {
            Map<String, List<Media>> mediaMap = container.getAll();

            ObservableList<Node> nodes = ((Pane) root).getChildren();
            ScrollPane scrollPane = (ScrollPane) nodes.filtered(node -> node instanceof ScrollPane).get(0);
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);

            for (Map.Entry<String, List<Media>> entry : mediaMap.entrySet()) {
                Label label = new Label(entry.getKey());
                label.setFont(Font.font(24));
                box.getChildren().add(label);
                TableView<Media> table = createTable(entry.getValue());
                box.getChildren().add(table);
            }
            scrollPane.setContent(box);
        }
    }

    private TableView<Media> createTable(List<Media> list) {
        TableView<Media> table = new TableView<>();
        table.setEditable(true);

        TableColumn<Media, String> name = new TableColumn<>("Name");
        name.setMinWidth(300);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(name);


        TableColumn<Media, String> season = new TableColumn<>("Season");
        season.setMinWidth(100);
        season.setCellValueFactory(new PropertyValueFactory<>("season"));
        table.getColumns().add(season);

        TableColumn<Media, String> episode = new TableColumn<>("Episode");
        episode.setMinWidth(100);
        episode.setCellValueFactory(new PropertyValueFactory<>("episode"));
        table.getColumns().add(episode);

        table.getItems().setAll(list);

        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        return table;
    }


}
