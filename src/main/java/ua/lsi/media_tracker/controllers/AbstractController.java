package ua.lsi.media_tracker.controllers;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.apache.log4j.Logger;

/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
public abstract class AbstractController implements Controller {
    private static Logger LOG = Logger.getLogger(AbstractController.class);

    private Node view;

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    protected void clearLabelAfterDelay(Label label, final int millis) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(millis);
                return null;
            }
        };
        task.setOnSucceeded(event -> label.setText(""));
        task.setOnFailed(event -> LOG.error(event.getSource().getException()));
        new Thread(task).start();
    }
}