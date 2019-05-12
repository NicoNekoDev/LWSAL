package ro.nicuch.lwsal.types;

import com.google.gson.JsonElement;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public abstract class Animation implements Cloneable {
    private final AnimationTypeEnum animationType;

    protected Animation(AnimationTypeEnum animationType) {
        this.animationType = animationType;
    }

    /**
     * Get what type of animation is
     *
     * @return The type of the animation
     */
    public final AnimationTypeEnum getAnimationType() {
        return this.animationType;
    }

    /**
     * The text of the animation
     *
     * @return The text
     */
    public abstract String getText();

    /**
     * The text of the animation
     * <p>
     * Uses {@link PlaceholderAPI#setPlaceholders(Player, String)} for placeholders
     *
     * @param player The player
     * @return The text
     */
    public abstract String getText(Player player);

    /**
     * Get the json value equivalent of this animation
     *
     * @return The json value
     */
    public abstract JsonElement getJson();

    /**
     * Update the animation
     */
    public abstract void update();

    /**
     * Clone animations
     *
     * @return A clone of animations
     */
    @Override
    public abstract Animation clone();

}
