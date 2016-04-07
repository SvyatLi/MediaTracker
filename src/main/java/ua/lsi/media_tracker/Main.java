package ua.lsi.media_tracker;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lsi.media_tracker.controllers.MediaTrackerController;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Service
public class Main extends AbstractJavaFxApplicationSupport {

    @Autowired
    private ConfigurationControllers.View view;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Media Tracker");
        primaryStage.setScene(new Scene(view.getView()));
        primaryStage.show();

        MediaTrackerController mediaTrackerController = context.getBean(MediaTrackerController.class);
        mediaTrackerController.setStage(primaryStage);
        mediaTrackerController.autoLoad();
    }
}
