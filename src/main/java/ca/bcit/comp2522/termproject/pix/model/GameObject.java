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
     * @param w the width of the game object as a double
     * @param h the height of the game object as a double
     * @param type the type of the game object
     * @param name the name of the game object
     * @param url the image path of the game object
     */
    public GameObject(final double x, final double y, final double w, final double h,
                      final ObjectType type, final T name, final String url) {
        this(x, y, type, name, url);
        this.setFitWidth(w);
        this.setFitHeight(h);
    }

    /**
     * Constructs a GameObject.
     *
     * @param x the x-coordinate of the game object as a double
     * @param y the y-coordinate of the game object as a double
     * @param type the type of the game object
     * @param name the name of the game object
     * @param url the image path of the game object
     */
    public GameObject(final double x, final double y, final ObjectType type, final T name, final String url) {
        super(new Image((String.valueOf(MainApplication.class.getResource(url)))));
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.objectName = name;
        this.objectType = type;
    }

    /**
     * Gets the name of the object.
     *
     * @return the name of the object
     */
    public final T getName() {
        return this.objectName;
    }


    /**
     * Gets the type of the object.
     *
     * @return the type of the object
     */
    public final ObjectType getType() {
        return this.objectType;
    }
}
