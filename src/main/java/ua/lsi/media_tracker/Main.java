package ua.lsi.media_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.lsi.media_tracker.controllers.MediaTrackerController;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class Main extends Application {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("MediaTracker-config.xml");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/media_tracker.fxml"));
        AnchorPane root = loader.load();
        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.show();

        MediaTrackerController mediaTrackerController = loader.getController();
        mediaTrackerController.setStage(primaryStage);
        mediaTrackerController.autoLoad();
    }
}
