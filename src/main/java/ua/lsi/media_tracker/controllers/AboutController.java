package ua.lsi.media_tracker.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by LSI on 27.04.2016.
 *
 * @author LSI
 */
@Component
public class AboutController extends AbstractController {
    private static Logger LOG = Logger.getLogger(AddItemController.class);

    public void openGithubInBrowser() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SvyatLi/MediaTracker"));
        } catch (IOException | URISyntaxException e) {
            LOG.error(e);
        }
    }
}
