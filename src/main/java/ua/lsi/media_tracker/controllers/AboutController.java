package ua.lsi.media_tracker.controllers;

import lombok.extern.log4j.Log4j;
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
@Log4j
public class AboutController extends AbstractController {

    public void openGithubInBrowser() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SvyatLi/MediaTracker"));
        } catch (IOException | URISyntaxException e) {
            log.error(e);
        }
    }
}
