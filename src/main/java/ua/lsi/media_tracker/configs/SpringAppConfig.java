package ua.lsi.media_tracker.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by LSI on 07.04.2016.
 *
 * @author LSI
 */
@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"ua.lsi.media_tracker.*"})
public class SpringAppConfig {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

//    @Bean
//    public DataSource dataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
//        dataSourceBuilder.url("jdbc:sqlite:your.db");
//        return dataSourceBuilder.build();
//    }
}
