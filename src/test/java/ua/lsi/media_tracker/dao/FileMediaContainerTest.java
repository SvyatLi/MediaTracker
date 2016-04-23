package ua.lsi.media_tracker.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.enums.SaveType;
import ua.lsi.media_tracker.model.Media;
import ua.lsi.media_tracker.utils.FileParserAndSaver;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.lsi.media_tracker.enums.MessageCode.*;

/**
 * Created by LSI on 30.03.2016.
 *
 * @author LSI
 */
public class FileMediaContainerTest {

    FileMediaContainer container = new FileMediaContainer();
    Messages messages;
    FileProvider fileProviderMock;
    Settings settingsMock;
    FileParserAndSaver fileParserAndSaverMock;

    @Before
    public void setUp() {
        messages = new Messages();
        container.setMessages(messages);
        fileProviderMock = mock(FileProvider.class);
        container.setFileProvider(fileProviderMock);
        settingsMock = mock(Settings.class);
        container.setSettings(settingsMock);
        fileParserAndSaverMock = mock(FileParserAndSaver.class);
        container.setFileParserAndSaver(fileParserAndSaverMock);
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testTryLoadFromSavedResource_loadDisabled() throws Exception {
        when(settingsMock.isAutomaticLoadEnabled()).thenReturn(false);
        String returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessage(AUTO_LOAD_DISABLED), returnedMessage);
    }

    @Test
    public void testTryLoadFromSavedResource_fileNotSet() throws Exception {
        when(settingsMock.isAutomaticLoadEnabled()).thenReturn(true);
        String returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessage(AUTO_LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testTryLoadFromSavedResource_correctFileSet() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(settingsMock.isAutomaticLoadEnabled()).thenReturn(true);
        when(settingsMock.getDefaultInfoFile()).thenReturn(file);
        String returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessageRelatedToFile(AUTO_LOAD_SUCCESSFUL, file), returnedMessage);
    }

    @Test
    public void testLoadInformation_nullFile() throws Exception {
        when(fileProviderMock.getFileForLoad()).thenReturn(null);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testLoadInformation_notExistingFile() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testLoadInformation_correctFile() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessageRelatedToFile(LOAD_SUCCESSFUL, file), returnedMessage);
    }

    @Test
    public void testGetSectionToMediaMap_containerIsNotSetUp() throws Exception {
        Map<String, List<Media>> resultMap = container.getSectionToMediaMap();
        Assert.assertNull(resultMap);
    }

    @Test
    public void testGetSectionToMediaMap_containerSetCorrect() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        Map<String, List<Media>> mediaMap = new HashMap<>();
        mediaMap.put("default", new ArrayList<Media>() {{
            add(new Media());
        }});
        when(fileParserAndSaverMock.getMapOfMediaFromFile(file)).thenReturn(mediaMap);
        container.loadInformation();
        Map<String, List<Media>> resultMap = container.getSectionToMediaMap();
        Assert.assertNotNull(resultMap);
        Assert.assertNotEquals(0, resultMap.size());
    }

    @Test
    public void testGetSectionToMediaMap_containerSetEmpty() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        container.loadInformation();
        Map<String, List<Media>> resultMap = container.getSectionToMediaMap();
        Assert.assertNotNull(resultMap);
        Assert.assertEquals(0, resultMap.size());
        Assert.assertEquals(Collections.EMPTY_MAP, resultMap);
    }

    @Test
    public void testSaveMediaMap_fileToSaveNull() throws Exception {
        when(fileProviderMock.getFileForSave(any())).thenReturn(null);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL);
        Assert.assertEquals(messages.getMessage(SAVE_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testSaveMediaMap_fileToSaveNotExist_canBeCreated() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForSave(any())).thenReturn(file);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL);
        Assert.assertEquals(messages.getMessageRelatedToFile(SAVE_SUCCESSFUL, file), returnedMessage);
    }

    @Test
    public void testSaveMediaMap_fileToSaveExist() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForSave(any())).thenReturn(file);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL);
        Assert.assertEquals(messages.getMessageRelatedToFile(SAVE_SUCCESSFUL, file), returnedMessage);
    }
}