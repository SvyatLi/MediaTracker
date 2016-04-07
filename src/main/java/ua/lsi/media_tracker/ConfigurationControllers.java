package ua.lsi.media_tracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.lsi.media_tracker.controllers.MediaTrackerController;
import ua.lsi.media_tracker.controllers.SettingsDialogController;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LSI on 08.04.2016.
 *
 * @author LSI
 */
@Configuration
public class ConfigurationControllers {

    @Bean(name = "mediaTrackerView")
    public View getMediaTrackerView() throws IOException {
        return loadView("view/media_tracker.fxml");
    }

    /**
     * Именно благодаря этому методу мы добавили контроллер в контекст спринга,
     * и заставили его произвести все необходимые инъекции.
     */
    @Bean
    public MediaTrackerController mediaTrackerController() throws IOException {
        return (MediaTrackerController) getMediaTrackerView().getController();
    }

    /**
     * Самый обыкновенный способ использовать FXML загрузчик.
     * Как раз-таки на этом этапе будет создан объект-контроллер,
     * произведены все FXML инъекции и вызван метод инициализации контроллера.
     */
    protected View loadView(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new View(loader.getRoot(), loader.getController());
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }

    /**
     * Класс - оболочка: контроллер мы обязаны указать в качестве бина,
     * а view - представление, нам предстоит использовать в точке входа {@link Main}.
     */
    public class View {
        private Parent view;
        private Object controller;

        public View(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }

}
