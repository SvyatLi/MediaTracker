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

import javax.swing.*;
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
    private static JDialog dialog;

    public static void main(String[] args) {
        new Thread(() -> {
            JOptionPane optionPane = new JOptionPane(
                    "Application is loading (maybe using Spring Data was a little overkill)\nThis window will be closed on app UI load" ,
                    JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            dialog = optionPane.createDialog("Don't worry");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }).start();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
                mediaTrackerController.loadDataFromDraggedFile(file);
                log.info("Data loaded from file : " + file.getAbsolutePath());
            }
            event.setDropCompleted(success);
            event.consume();
        });

        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/view/images/icon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        mediaTrackerController.init(primaryStage);
        mediaTrackerController.autoLoad();
        dialog.dispose();
    }

    @Override
    public void stop() {
        mediaTrackerController.promptSaveOnClose();
    }
}
