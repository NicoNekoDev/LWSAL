package ro.nicuch.lwsal.types;

import com.google.gson.JsonPrimitive;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

/**
 * An text animation
 * This just store an {@link String}
 */
public class AnimationText extends Animation {
    private String text = ""; //Default text, makes NullPointerException not to trigger

    /**
     * Create an animation with an empty text
     */
    public AnimationText() {
        super(AnimationTypeEnum.TEXT);
    }

    /**
     * Create an animation with the given text
     *
     * @param text The text
     */
    public AnimationText(String text) {
        this();
        this.text = text;
    }

    /**
     * Set the text for the animation
     *
     * @param text The text
     * @return the AnimationText
     */
    public AnimationText setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Get the text of this animation
     *
     * @return The text
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Get the text of this animation
     * <p>
     * Uses {@link PlaceholderAPI#setPlaceholders(Player, String)} for placeholders
     *
     * @param player The player
     * @return The text
     */
    @Override
    public String getText(Player player) {
        return PlaceholderAPI.setPlaceholders(player, this.text);
    }

    /**
     * Get the json value equivalent of this animation
     *
     * @return The json value
     */
    @Override
    public JsonPrimitive getJson() {
        return new JsonPrimitive(this.text);
    }

    /**
     * Update the animation
     * <p>
     * Does nothing for {@link AnimationText}
     */
    @Override
    public void update() {
    }

    /**
     * Clone this animation
     *
     * @return A clone of this animation
     */
    @Override
    public AnimationText clone() {
        return new AnimationText(this.text);
    }
}
