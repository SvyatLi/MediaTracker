package ua.lsi.media_tracker.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by LSI on 07.04.2016.
 *
 * @author LSI
 */
@Configuration
@ComponentScan(basePackages = {"ua.lsi.media_tracker.*"})
public class SpringAppConfig {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
