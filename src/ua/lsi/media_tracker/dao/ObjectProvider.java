package ua.lsi.media_tracker.dao;

import ua.lsi.media_tracker.enums.StorageType;

/**
 * Created by LSI on 26.03.2016.
 *
 * @author LSI
 */
public class ObjectProvider {

    private static class FileMediaContainer{
        private static final MediaContainer CONTAINER = new FileLoader();
    }


    public static MediaContainer getMediaContainer(StorageType type){
        switch (type){
            case FILE:
                return FileMediaContainer.CONTAINER;
            default:
                return FileMediaContainer.CONTAINER;
        }
    }
}
