package ua.lsi.media_tracker;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.lsi.media_tracker.controllers.MediaTrackerController;

import java.io.File;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Log4j
@SpringBootApplication
public class Main extends Application {
    public static MediaTrackerController mediaTrackerController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mediaTrackerController = (MediaTrackerController) SpringFXMLLoader.load("/view/media_tracker.fxml");
        Scene scene = new Scene((Parent) mediaTrackerController.getView());
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        scene.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && (db.getFiles().size() == 1)) {
                event.acceptTransferModes(TransferMode.ANY);
            } else {
                event.consume();
            }
        });

        // Dropping over surface
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0);
                mediaTrackerController.loadDataFromFile(file);
                log.info("Data loaded from file : " + file.getAbsolutePath());
            }
            event.setDropCompleted(success);
            event.consume();
        });

        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/view/images/icon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.show();

        mediaTrackerController.init(primaryStage);
        mediaTrackerController.autoLoad();
    }

}
