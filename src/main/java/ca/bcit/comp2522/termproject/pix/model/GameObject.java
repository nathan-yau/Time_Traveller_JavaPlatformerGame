package ca.bcit.comp2522.termproject.pix.model;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an object in the game that has coordinates.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 * @param <T> the type of the object
 */
public abstract class GameObject<T extends Enum<T>> extends ImageView {
    // The name of the object.
    private final T objectName;

    // The type of the object.
    private final ObjectType objectType;

    /**
     * Constructs a GameObject.
     *
     * @param x the x-coordinate of the game object as a double
     * @param y the y-coordinate of the game object as a double
     * @param w
     * @param h
     * @param type
     * @param name
     * @param url
     */
    public GameObject(final double x, final double y, final double w, final double h, final ObjectType type, final T name, final String url) {
        this(x, y, type, name, url);
        this.setFitWidth(w);
        this.setFitHeight(h);
    }

    public GameObject(final double x, final double y, final ObjectType type, final T name, final String url) {
        super(new Image((String.valueOf(MainApplication.class.getResource(url)))));
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.objectName = name;
        this.objectType = type;
    }
    public final T getName() {
        return this.objectName;
    }

    public final ObjectType getType() {
        return this.objectType;
    }
}
