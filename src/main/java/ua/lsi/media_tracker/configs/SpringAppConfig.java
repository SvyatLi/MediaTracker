package ua.lsi.media_tracker.configs;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ua.lsi.media_tracker.utils.SettingsProvider;

import javax.sql.DataSource;

/**
 * Created by LSI on 07.04.2016.
 *
 * @author LSI
 */
@Configuration
@Log4j
public class SpringAppConfig {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:" + SettingsProvider.getUserDataDirectory() + "media_tracker.db");
        return dataSourceBuilder.build();
    }
}
