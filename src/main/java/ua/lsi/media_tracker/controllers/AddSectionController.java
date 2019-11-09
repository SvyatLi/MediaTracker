package ua.lsi.media_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by LSI on 17.04.2016.
 *
 * @author LSI
 */
@Component
@RequiredArgsConstructor
public class AddSectionController extends AbstractController {

    @FXML
    public Label addingStatusLabel;
    @FXML
    public TextField sectionTextField;

    private final MediaTrackerController mediaTrackerController;

    @FXML
    public void addSection() {
        String newSection = sectionTextField.getText();
        if (newSection == null || newSection.isEmpty()) {
            addingStatusLabel.setText("Wrong section name");
            clearLabelAfterDelay(addingStatusLabel, 2000);
        } else {
            mediaTrackerController.addNewItem(newSection, null);
            ((Stage) sectionTextField.getScene().getWindow()).close();
        }
    }
}
