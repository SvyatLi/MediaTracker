package ua.lsi.media_tracker.creators;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import ua.lsi.media_tracker.dao.FileMediaContainer;

/**
 * Created by LSI on 03.04.2016.
 *
 * @author LSI
 */
@Repository
public class BeanCreator {

    @Bean
    public FileProvider getFileProvider() {
        return new FileProvider();
    }

    @Bean
    public Settings getSettings() {
        return new Settings.Builder().build();
    }

    @Bean
    public ObjectProvider getObjectProvider() {
        return new ObjectProvider();
    }

    @Bean
    public Messages getMessages() {
        return new Messages();
    }

    @Bean
    public MessageCreator getMessageCreator() {
        return new MessageCreator();
    }

    @Bean
    public FileMediaContainer getFileMediaContainer() {
        return new FileMediaContainer();
    }
}
