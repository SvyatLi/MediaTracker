package ua.lsi.media_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/media_tracker.fxml"));
        AnchorPane root = loader.load();
        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.show();

        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        controller.autoLoad();
    }
}
