package ua.lsi.media_tracker.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import ua.lsi.media_tracker.creators.FileProvider;
import ua.lsi.media_tracker.creators.MessageCreator;
import ua.lsi.media_tracker.enums.MessageCode;

import java.io.File;

import static org.mockito.Mockito.*;
import static ua.lsi.media_tracker.enums.MessageCode.LOAD_SUCCESSFUL;

/**
 * Created by LSI on 30.03.2016.
 *
 * @author LSI
 */
public class FileMediaContainerTest {

    FileMediaContainer container = new FileMediaContainer();

    @BeforeClass
    public static void mockMethods() {

    }

    @Test
    public void testTryLoadFromSavedResource() throws Exception {

    }

    @Test
    public void testLoadInformation() throws Exception {
        FileProvider fileProvider = Mockito.mock(FileProvider.class);
//        FileProvider fileProvider = spy(FileProvider.class);
        File file = new File("F:\\torrent\\z_Serials.txt");
//        doReturn(file).when(fileProvider).getFileForLoad();
        when(fileProvider.getFileForLoad()).thenReturn(file);
        container.setFileProvider(fileProvider);
        String returnedMessage = container.loadInformation();
        MessageCreator messageCreator = new MessageCreator();

        Assert.assertEquals(messageCreator.getMessageRelatedToCodeAndFile(LOAD_SUCCESSFUL, file),returnedMessage);
//        file = new File("F:\\torrent\\notExist.txt");
//        returnedMessage = container.loadInformation();
//        Assert.assertEquals(Messages.getInstance().getMessage(LOAD_UNSUCCESSFUL),returnedMessage);
//        returnedMessage = container.loadInformation();
//        Assert.assertEquals(Messages.getInstance().getMessage(LOAD_UNSUCCESSFUL),returnedMessage);
    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testSaveAll() throws Exception {

    }
}