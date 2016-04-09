package ua.lsi.media_tracker.controllers;

import javafx.scene.Node;
/**
 * Created by LSI on 09.04.2016.
 *
 * @author LSI
 */
public interface Controller {
    Node getView();
    void setView (Node view);
}
