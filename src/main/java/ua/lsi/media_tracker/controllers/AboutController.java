package ua.lsi.media_tracker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ua.lsi.media_tracker.utils.Version;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by LSI on 27.04.2016.
 *
 * @author LSI
 */
@Component
@Log4j
public class AboutController extends AbstractController implements Initializable {
    @FXML
    public Label versionLabel;

    public void openGithubInBrowser() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SvyatLi/MediaTracker"));
        } catch (IOException | URISyntaxException e) {
            log.error(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText("Version: " + Version.getVersion());
    }
}
