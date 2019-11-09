package ua.lsi.media_tracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import ua.lsi.media_tracker.controllers.Controller;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
@Log4j
public class SpringFXMLLoader {

    private static final ApplicationContext APPLICATION_CONTEXT = SpringApplication.run(Main.class);

    public static Controller load(String url) {
        try (InputStream fxmlStream = SpringFXMLLoader.class.getResourceAsStream(url)) {

            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(APPLICATION_CONTEXT::getBean);

            Node view = loader.load(fxmlStream);
            Controller controller = loader.getController();
            controller.setView(view);

            return controller;
        } catch (IOException e) {
            log.error("Can't load resource", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T getBeanFromContext(Class<T> clazz) {
        return APPLICATION_CONTEXT.getBean(clazz);
    }
}
