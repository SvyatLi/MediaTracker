package ua.lsi.media_tracker.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;
import ua.lsi.media_tracker.model.Media;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    FileProvider fileProvider;
    Settings settingsMock;

    @Before
    public void init() {
        messages = new Messages();
        container.setMessages(messages);
        fileProvider = Mockito.mock(FileProvider.class);
        container.setFileProvider(fileProvider);
        settingsMock = mock(Settings.class);
        container.setSettings(settingsMock);
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
        when(fileProvider.getFileForLoad()).thenReturn(null);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testLoadInformation_notExistingFile() throws Exception {
        File file = new File("/notExist.txt");
        when(fileProvider.getFileForLoad()).thenReturn(file);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testLoadInformation_correctFile() throws Exception {
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(fileProvider.getFileForLoad()).thenReturn(file);
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
        when(fileProvider.getFileForLoad()).thenReturn(file);
        container.loadInformation();
        Map<String, List<Media>> resultMap = container.getSectionToMediaMap();
        Assert.assertNotNull(resultMap);
        Assert.assertNotEquals(0,resultMap.size());
    }

    @Test
    public void testGetSectionToMediaMap_containerSetEmpty() throws Exception {
        File file = new File("/notExist.txt");
        when(fileProvider.getFileForLoad()).thenReturn(file);
        container.loadInformation();
        Map<String, List<Media>> resultMap = container.getSectionToMediaMap();
        Assert.assertNotNull(resultMap);
        Assert.assertEquals(0, resultMap.size());
        Assert.assertEquals(Collections.EMPTY_MAP, resultMap);
    }

    @Test
    @Ignore("rewrite this to make testable")
    public void testSaveMediaMap() throws Exception {
        container.saveMediaMap(); //FIXME: rewrite this to make testable
        Assert.fail("not written");
    }
}