package ua.lsi.media_tracker.utils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.enums.MessageCode;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
public class FileParserAndSaverTest {

    FileParserAndSaver fileParserAndSaver;
    Messages messages;

    @Before
    public void setUp() throws Exception {
        fileParserAndSaver = new FileParserAndSaver();
        messages = new Messages();
        fileParserAndSaver.setMessages(messages);
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "canBeCreated.txt");
        if (file.exists()){
            file.delete();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetMapOfMediaFromFile_nullFile() throws Exception {
        fileParserAndSaver.getMapOfMediaFromFile(null);
    }

    @Test
    public void testGetMapOfMediaFromFile_notExistingFile() throws Exception {
        File file = new File("/notExist.txt");
        Map<String, List<Media>> result = fileParserAndSaver.getMapOfMediaFromFile(file);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetMapOfMediaFromFile_correctFile() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        Map<String, List<Media>> result = fileParserAndSaver.getMapOfMediaFromFile(file);
        assertNotNull(result);
        assertNotEquals(0, result.size());
    }

    @Test(expected = NullPointerException.class)
    public void testSaveMapToFile_fileNull() throws Exception {
        fileParserAndSaver.saveMapToFile(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveMapToFile_fileNotExist_nullMap() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "canBeCreated.txt");
        file.setWritable(true);
        fileParserAndSaver.saveMapToFile(null, file);
    }

    @Test
    public void testSaveMapToFile_fileNotExistAndNOTWritable() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "readOnly.txt");
        file.setWritable(false);
        String returnedMessage = fileParserAndSaver.saveMapToFile(null, file);
        assertEquals(messages.getMessage(MessageCode.SAVE_ERROR), returnedMessage);
    }

    @Test
    public void testSaveMapToFile_fileExist_emptyMap() throws Exception {
        URL url = this.getClass().getResource("/saveTo.txt");
        File file = new File(url.getFile());
        String returnedMessage = fileParserAndSaver.saveMapToFile(new HashMap<>(), file);
        assertEquals(messages.getMessage(MessageCode.SAVE_NOT_SAVED), returnedMessage);
    }

    @Test
    public void testSaveMapToFile_fileExist_notEmptyMap() throws Exception {
        URL url = this.getClass().getResource("/saveTo.txt");
        File file = new File(url.getFile());

        Map<String, List<Media>> mediaMap = new HashMap<>();
        Media media = new Media();
        media.setName("Test");
        mediaMap.put("default", new ArrayList<Media>() {{
            add(media);
        }});
        String returnedMessage = fileParserAndSaver.saveMapToFile(mediaMap, file);
        assertEquals(messages.getMessageRelatedToFile(MessageCode.SAVE_SUCCESSFUL, file), returnedMessage);
    }
}