package ua.lsi.media_tracker.utils;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by LSI on 30.03.2016.
 *
 * @author LSI
 */
public class FileProvider {

    public static FileProvider getInstance() {
        return FileProviderHolder.INSTANCE;
    }

    public File getFileForLoad() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    public File getFileForSave(File loadedFromFile) {
        FileChooser fileChooser = new FileChooser();
        if (loadedFromFile != null) {
            fileChooser.setInitialDirectory(loadedFromFile.getParentFile());
            fileChooser.setInitialFileName(loadedFromFile.getName());
        }
        return fileChooser.showSaveDialog(null);
    }

    private static class FileProviderHolder {
        public static final FileProvider INSTANCE = new FileProvider();
    }
}
