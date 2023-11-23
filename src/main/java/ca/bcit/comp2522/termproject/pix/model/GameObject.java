package ca.bcit.comp2522.termproject.pix.model;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

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
    private final T objectSubtype;

    // The type of the object.
    private final ObjectType objectType;

    /**
     * Constructs a GameObject with dimensions.
     *
     * @param x the x-coordinate of the game object as a double
     * @param y the y-coordinate of the game object as a double
     * @param w the width of the game object as a double
     * @param h the height of the game object as a double
     * @param type the type of the game object as an ObjectType
     * @param subtype the subtype of the game object as a T
     * @param url the url of the game object as a String
     */
    public GameObject(final double x, final double y, final double w, final double h, final ObjectType type,
                      final T subtype, final String url) {
        this(x, y, type, subtype, url);
        this.setFitWidth(w);
        this.setFitHeight(h);
    }

    /**
     * Constructs a GameObject without dimensions.
     *
     * @param x the x-coordinate of the game object as a double
     * @param y the y-coordinate of the game object as a double
     * @param type the type of the game object as an ObjectType
     * @param subtype the subtype of the game object as a T
     * @param url the url of the game object as a String
     */
    public GameObject(final double x, final double y, final ObjectType type, final T subtype, final String url) {
        super(new Image((String.valueOf(MainApplication.class.getResource(url)))));
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.objectSubtype = subtype;
        this.objectType = type;
    }

    /**
     * Gets the type of the game object.
     *
     * @return the subtype of the game object as an ObjectType
     */
    public final ObjectType getType() {
        return this.objectType;
    }

    /**
     * Gets the subtype of the game object.
     *
     * @return the subtype of the game object as a T
     */
    public final T getSubtype() {
        return this.objectSubtype;
    }


}
