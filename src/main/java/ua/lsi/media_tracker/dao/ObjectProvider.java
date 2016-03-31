package ua.lsi.media_tracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.enums.StorageType;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Component
public class ObjectProvider {

    private static ObjectProvider objectProvider;

    private FileMediaContainer fileMediaContainer;

    @Bean
    public ObjectProvider getObjectProvider(){
        return new ObjectProvider();
    }

    @Autowired
    public void setObjectProvider(ObjectProvider objectProvider){
        ObjectProvider.objectProvider = objectProvider;
    }


    @Autowired
    public void setFileMediaContainer(FileMediaContainer fileMediaContainer) {
        this.fileMediaContainer = fileMediaContainer;
    }


    public static MediaContainer getMediaContainer(StorageType type) {
        switch (type) {
            case FILE:
                return objectProvider.fileMediaContainer;
            default:
                return objectProvider.fileMediaContainer;
        }
    }
}
