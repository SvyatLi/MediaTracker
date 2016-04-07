package ua.lsi.media_tracker;

import javafx.application.Application;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.lsi.media_tracker.configs.SpringAppConfig;

/**
 * Created by LSI on 08.04.2016.
 *
 * @author LSI
 */
public abstract class AbstractJavaFxApplicationSupport extends Application {

    protected ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = new AnnotationConfigApplicationContext(SpringAppConfig.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }
}
