package ua.lsi.media_tracker.creators;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

/**
 * Created by LSI on 03.04.2016.
 *
 * @author LSI
 */
@Repository
//TODO: implement FactoryBean
public class BeanCreator {

    @Bean
    public Settings getSettings() {
        return new Settings.Builder().build();
    }
}
