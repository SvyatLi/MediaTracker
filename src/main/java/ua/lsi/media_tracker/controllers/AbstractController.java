package ua.lsi.media_tracker.controllers;

import javafx.scene.Node;

/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
public abstract class AbstractController implements Controller {
    private Node view;

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }
}