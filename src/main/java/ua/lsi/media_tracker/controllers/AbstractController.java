package ua.lsi.media_tracker.controllers;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j;

/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
@Log4j
public abstract class AbstractController implements Controller {

    private Node view;
    private Thread thread;

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    protected void clearLabelAfterDelay(Label label, final int millis) {
        if (thread != null) {
            thread.interrupt();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(millis);
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            label.setText("");
            thread = null;
        });
        task.setOnFailed(event -> log.error(event.getSource().getException()));
        thread = new Thread(task);
        thread.start();
    }
}