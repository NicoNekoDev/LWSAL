package ro.nicuch.lwsal.types;

import com.google.gson.JsonArray;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.LinkedList;

/**
 * A bundle of animations
 * <p>
 * This is used to store multiple animations
 */
public class AnimationCompound extends Animation {
    private LinkedList<Animation> compound = new LinkedList<>();

    /**
     * Create a bundle with no animations
     */
    public AnimationCompound() {
        super(AnimationTypeEnum.COMPOUND);
    }

    /**
     * Create a bundle with the given animations
     * <p>
     * The order of the {@code getText()} is determied by the order of the {@link LinkedList}
     *
     * @param animations The animations
     */
    public AnimationCompound(LinkedList<Animation> animations) {
        this();
        this.compound = animations;
    }

    /**
     * Check if the animation bundle is empty
     *
     * @return if the bundle is empty
     */
    public boolean isEmpty() {
        return this.compound.isEmpty();
    }

    /**
     * Add an animation to the bundle
     * The animation will be added last on the list
     *
     * @param anim The animation
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimation(Animation anim) {
        this.compound.addLast(anim);
        return this;
    }

    /**
     * Add an animation to the bundle
     * The animation will be added to the index point
     *
     * @param anim  The animation
     * @param index The index
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimation(Animation anim, int index) {
        this.compound.add(index, anim);
        return this;
    }

    /**
     * Add animations to the bundle
     * The animations will be added last on the list
     *
     * @param anim The animations
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimations(Animation... anim) {
        LinkedList<Animation> list = new LinkedList<>();
        for (Animation animation : anim)
            list.addLast(animation);
        return this.addAnimations(list);
    }

    /**
     * Add animations to the bundle
     * The animations will be added to the index point
     *
     * @param anim  The animations
     * @param index The index
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimations(int index, Animation... anim) {
        LinkedList<Animation> list = new LinkedList<>();
        for (Animation animation : anim)
            list.addLast(animation);
        return this.addAnimations(list, index);
    }

    /**
     * Add the animations to the bundle
     * The animations will be added last on the list
     *
     * @param anim The animations
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimations(LinkedList<Animation> anim) {
        this.compound.addAll(anim);
        return this;
    }

    /**
     * Add the animations to the bundle
     * The animations will be added to the index point
     *
     * @param anim  The animations
     * @param index The index
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound addAnimations(LinkedList<Animation> anim, int index) {
        this.compound.addAll(index, anim);
        return this;
    }

    /**
     * Overwrite all the animations
     *
     * @param anim The animations
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound setAnimations(Animation... anim) {
        LinkedList<Animation> list = new LinkedList<>();
        for (Animation animation : anim)
            list.addLast(animation);
        return this.setAnimations(list);
    }

    /**
     * Overwrite all the animations
     *
     * @param anim The animations
     * @return The caller {@link AnimationCompound}
     */
    public AnimationCompound setAnimations(LinkedList<Animation> anim) {
        this.compound = anim;
        return this;
    }

    /**
     * Get the animations text in one
     *
     * @return The text of all animations in one
     */
    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();
        this.compound.forEach((Animation anim) -> builder.append(anim.getText()));
        return builder.toString();
    }

    /**
     * Get the animations text in one
     * <p>
     * Uses {@link PlaceholderAPI#setPlaceholders(Player, String)} for placeholders
     *
     * @param player The player
     * @return The text of all animations in one
     */
    @Override
    public String getText(Player player) {
        StringBuilder builder = new StringBuilder();
        this.compound.forEach((Animation anim) -> builder.append(anim.getText(player)));
        return builder.toString();
    }

    /**
     * Get the json value equivalent of this animation
     * It contains all the animations in the bundle
     *
     * @return The json value
     */
    @Override
    public JsonArray getJson() {
        JsonArray jsonArray = new JsonArray();
        this.compound.forEach((Animation anim) -> jsonArray.add(anim.getJson()));
        return null;
    }

    /**
     * Update all the animations present in the bundle
     */
    public void update() {
        this.compound.forEach(Animation::update);
    }

    /**
     * Clone the bundle
     *
     * @return A clone of the bundle
     */
    @Override
    public AnimationCompound clone() {
        LinkedList<Animation> list = new LinkedList<>();
        this.compound.forEach((Animation anim) -> list.addLast(anim.clone()));
        return new AnimationCompound(list);
    }
}
