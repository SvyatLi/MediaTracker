package ua.lsi.media_tracker;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.dao.ObjectProvider;
import ua.lsi.media_tracker.emuns.StorageType;
import ua.lsi.media_tracker.model.Media;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Controller implements Initializable {

//    @FXML
//    private TableView<Media> tableView;
//    @FXML
//    private TableColumn<Media, String> section;
//    @FXML
//    private TableColumn<Media, String> name;
//    @FXML
//    private TableColumn<Media, String> season;
//    @FXML
//    private TableColumn<Media, String> episode;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        section.setCellValueFactory(new PropertyValueFactory<>("section"));
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        season.setCellValueFactory(new PropertyValueFactory<>("season"));
//        episode.setCellValueFactory(new PropertyValueFactory<>("episode"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadData(ActionEvent e) {
        MediaContainer container = ObjectProvider.getMediaContainer(StorageType.FILE);
        container.init();

        Scene scene = stage.getScene();
        Parent root = scene.getRoot();

        List<Media> list = container.getAll();

        TableView<Media> table = createTable(list);



        if (root instanceof Pane) {
            ObservableList<Node> nodes = ((Pane) root).getChildren();
            ScrollPane scrollPane = (ScrollPane) nodes.filtered(node -> node instanceof ScrollPane).get(0);
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            Label label = new Label("First");
            label.setFont(Font.font(24));
            box.getChildren().add(label);
            box.getChildren().add(table);
            box.getChildren().add(createTable(Collections.EMPTY_LIST));//need reworking of data holder
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
        return table;
    }


}
