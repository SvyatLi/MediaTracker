package ua.lsi.media_tracker.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.Messages;
import ua.lsi.media_tracker.creators.Settings;

import java.io.File;
import java.net.URL;

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

    @Before
    public void init() {
        messages = new Messages();
        container.setMessages(messages);
    }

    @Test
    public void testTryLoadFromSavedResource() throws Exception {
        Settings settingsMock = mock(Settings.class);
        container.setSettings(settingsMock);

        when(settingsMock.isAutomaticLoadEnabled()).thenReturn(false);
        String returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessage(AUTO_LOAD_DISABLED), returnedMessage);

        when(settingsMock.isAutomaticLoadEnabled()).thenReturn(true);
        returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessage(AUTO_LOAD_UNSUCCESSFUL), returnedMessage);

        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());
        when(settingsMock.getDefaultInfoFile()).thenReturn(file);
        returnedMessage = container.tryLoadFromSavedResource();
        Assert.assertEquals(messages.getMessageRelatedToFile(AUTO_LOAD_SUCCESSFUL, file), returnedMessage);
    }

    @Test
    public void testLoadInformation() throws Exception {
        FileProvider fileProvider = Mockito.mock(FileProvider.class);
        URL url = this.getClass().getResource("/z_Serials.txt");
        File file = new File(url.getFile());

        when(fileProvider.getFileForLoad()).thenReturn(file);
        container.setFileProvider(fileProvider);
        String returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessageRelatedToFile(LOAD_SUCCESSFUL, file), returnedMessage);

        file = new File("/notExist.txt");
        when(fileProvider.getFileForLoad()).thenReturn(file);
        returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);

        when(fileProvider.getFileForLoad()).thenReturn(null);
        returnedMessage = container.loadInformation();
        Assert.assertEquals(messages.getMessage(LOAD_UNSUCCESSFUL), returnedMessage);
    }

    @Test
    public void testGetSectionToMediaMap() throws Exception {
        Assert.fail("not written");
    }

    @Test
    public void testSaveMediaMap() throws Exception {
        Assert.fail("not written");
    }
}