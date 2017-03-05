package ua.lsi.media_tracker.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.lsi.media_tracker.Main;
import ua.lsi.media_tracker.controllers.MediaTrackerController;
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
import static ua.lsi.media_tracker.enums.MessageCode.SAVE_SUCCESSFUL;
import static ua.lsi.media_tracker.enums.MessageCode.SAVE_UNSUCCESSFUL;

/**
 * Created by LSI on 30.03.2016.
 *
 * @author LSI
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FileMediaContainerTest {

    @Autowired
    FileMediaContainer container;

    @Autowired
    Messages messages;

    @MockBean
    FileProvider fileProviderMock;

    @MockBean
    Settings settingsMock;

    @Autowired
    FileParserAndSaver fileParserAndSaverMock;

    @Before
    public void setUp() {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        if (file.exists()) {
            file.delete();
        }
        Main.mediaTrackerController = mock(MediaTrackerController.class);
    }

    @Test
    public void testTryLoadFromSavedResource_loadDisabled() throws Exception {
        when(settingsMock.getAutomaticLoadEnabled()).thenReturn(false);
        Map<String, List<Media>> result = container.tryLoadFromSavedResource();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testTryLoadFromSavedResource_fileNotSet() throws Exception {
        when(settingsMock.getAutomaticLoadEnabled()).thenReturn(true);
        Map<String, List<Media>> result = container.tryLoadFromSavedResource();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testTryLoadFromSavedResource_correctFileSet() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(settingsMock.getAutomaticLoadEnabled()).thenReturn(true);
        when(settingsMock.getDefaultInfoFile()).thenReturn(file);
        Map<String, List<Media>> result = container.tryLoadFromSavedResource();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testLoadInformation_nullFile() throws Exception {
        when(fileProviderMock.getFileForLoad()).thenReturn(null);
        Map<String, List<Media>> result = container.loadInformation();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLoadInformation_notExistingFile() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        Map<String, List<Media>> result = container.loadInformation();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLoadInformation_correctFile() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        Map<String, List<Media>> returnedMessage = container.loadInformation();
        Assert.assertEquals(5, returnedMessage.size());
    }

    @Test
    public void testGetSectionToMediaMap_containerSetCorrect() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForLoad()).thenReturn(file);

        Map<String, List<Media>> resultMap = container.loadInformation();
        Assert.assertNotNull(resultMap);
        Assert.assertEquals(5, resultMap.size());
    }

    @Test
    public void testGetSectionToMediaMap_containerSetEmpty() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForLoad()).thenReturn(file);
        Map<String, List<Media>> resultMap = container.loadInformation();
        Assert.assertNotNull(resultMap);
        Assert.assertEquals(0, resultMap.size());
        Assert.assertEquals(Collections.EMPTY_MAP, resultMap);
    }

    @Test
    public void testSaveMediaMap_fileToSaveNull() throws Exception {
        when(fileProviderMock.getFileForSave(any())).thenReturn(null);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL, null);
        Assert.assertEquals(messages.getMessage(SAVE_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testSaveMediaMap_fileToSaveNotExist_canBeCreated() throws Exception {
        String resourceFolderPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(resourceFolderPath + "notExist.txt");
        when(fileProviderMock.getFileForSave(any())).thenReturn(file);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL, new LinkedHashMap<>());
        Assert.assertEquals(messages.getMessageRelatedToFile(SAVE_SUCCESSFUL, file), returnedMessage);
    }

    @Test
    public void testSaveMediaMap_fileToSaveExist() throws Exception {
        URL url = this.getClass().getResource("/EmptyFile.txt");
        File file = new File(url.getFile());
        when(fileProviderMock.getFileForSave(any())).thenReturn(file);
        String returnedMessage = container.saveMediaMap(SaveType.MANUAL, new LinkedHashMap<>());
        Assert.assertEquals(messages.getMessageRelatedToFile(SAVE_SUCCESSFUL, file), returnedMessage);
    }
}