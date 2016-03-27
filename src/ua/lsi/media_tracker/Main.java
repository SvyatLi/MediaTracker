package ua.lsi.media_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(new Scene(root, ((Pane) root).getPrefWidth(), ((Pane) root).getPrefHeight()));
        primaryStage.show();

        Controller controller = loader.getController();
        controller.setStage(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
