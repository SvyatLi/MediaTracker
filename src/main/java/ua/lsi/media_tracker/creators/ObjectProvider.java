package ua.lsi.media_tracker.creators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.dao.FileMediaContainer;
import ua.lsi.media_tracker.dao.MediaContainer;
import ua.lsi.media_tracker.enums.StorageType;

import javax.annotation.PostConstruct;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
@Component
public class ObjectProvider {

    private static ObjectProvider objectProvider;
    private ObjectProvider objectProviderToSaveInStatic;

    private FileMediaContainer fileMediaContainer;

    public ObjectProvider() {
        objectProviderToSaveInStatic = this;
    }

    public static MediaContainer getMediaContainer(StorageType type) {
        switch (type) {
            case FILE:
                return objectProvider.fileMediaContainer;
            default:
                return objectProvider.fileMediaContainer;
        }
    }

    @PostConstruct
    public void saveInstance() {
        ObjectProvider.objectProvider = objectProviderToSaveInStatic;
    }

    @Autowired
    public void setFileMediaContainer(FileMediaContainer fileMediaContainer) {
        this.fileMediaContainer = fileMediaContainer;
    }
}
