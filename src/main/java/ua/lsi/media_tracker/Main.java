package ua.lsi.media_tracker;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ua.lsi.media_tracker.controllers.MediaTrackerController;

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
        MediaTrackerController mediaTrackerController = (MediaTrackerController) SpringFXMLLoader.load("view/media_tracker.fxml");
        Scene scene = new Scene((Parent) mediaTrackerController.getView());
        scene.getStylesheets().add(getClass().getResource("view/style.css").toExternalForm());

        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("view/icon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        mediaTrackerController.init(primaryStage);
        mediaTrackerController.autoLoad();
    }
}
