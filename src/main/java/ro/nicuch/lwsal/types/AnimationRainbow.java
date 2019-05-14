package ro.nicuch.lwsal.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.utils.StringUtils;

/**
 * An rainbow animation!
 */
public class AnimationRainbow extends Animation {
    private Animation text = new AnimationText(); //Default animation
    private AnimationOptions options = new AnimationOptions(); //Default options
    private final String[] colors = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"}; //The colors of the rainbow animation
    private int nextUpdate = 0, color = 0; //Default values
    private String currentColor = "0"; //Default current color

    /**
     * Create an empty rainbow animation
     */
    public AnimationRainbow() {
        super(AnimationTypeEnum.RAINBOW);
    }

    /**
     * Create an animation with the given params
     *
     * @param text    The animation
     *                Can contain another animation inside
     * @param options The options of this animation
     */
    public AnimationRainbow(Animation text, AnimationOptions options) {
        this();
        this.text = text;
        this.options = options;
    }

    /**
     * Overwrite the animtion
     *
     * @param text The animation
     * @return The caller {@link AnimationRainbow}
     */
    public AnimationRainbow setText(Animation text) {
        this.text = text;
        return this;
    }

    /**
     * Get the options of this animation
     * <p>
     * This animation options:</p>
     * - {@link AnimationOptions.OptionIntEnum#UPDATE_TIME}
     * - {@link AnimationOptions.OptionBooleanEnum#STRIP_COLORS}
     *
     * @return The options {@link AnimationOptions}
     */
    public AnimationOptions getOptions() {
        return this.options;
    }

    /**
     * Set the options of this animation
     * <p>
     * This animation options:
     * - {@link AnimationOptions.OptionIntEnum#UPDATE_TIME}
     * - {@link AnimationOptions.OptionBooleanEnum#STRIP_COLORS}
     *
     * @param options The options
     * @return The caller {@link AnimationRainbow}
     */
    public AnimationRainbow setOptions(AnimationOptions options) {
        this.options = options;
        return this;
    }

    /**
     * This method will take the animation text
     * and apply the rainbow function
     *
     * @return The result of the text
     */
    @Override
    public String getText() {
        String text = this.text.getText();
        return this.getRainbowText(text);
    }

    private String getRainbowText(String text) {
        return "&" + currentColor +
                (this.options.getOptionBoolean(AnimationOptions.OptionBooleanEnum.STRIP_COLORS) ? this.stripColors(text) : text);
    }

    /**
     * This method will take the animation text
     * and apply the rainbow function
     * <p>
     * Uses {@link PlaceholderAPI#setPlaceholders(Player, String)} for placeholders
     *
     * @return The result of the text
     */
    @Override
    public String getText(Player player) {
        String text = this.text.getText(player);
        return this.getRainbowText(text);
    }

    /**
     * Get the json value equivalent of this animation
     *
     * @return The json value
     */
    @Override
    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive("rainbow"));
        jsonObject.add("text", this.text.getJson());
        return jsonObject;
    }

    /**
     * Update the animation
     */
    @Override
    public void update() {
        /*update timer start*/
        this.text.update(); //Update the text first.
        int update_time = this.options.getOptionInt(AnimationOptions.OptionIntEnum.UPDATE_TIME);
        if (update_time > 1) {
            if (this.nextUpdate < update_time - 1) {
                this.nextUpdate++;
                return;
            }
            this.nextUpdate = 0;
        }
        /*update timer end*/
        if (this.color < this.colors.length - 1)
            this.color++;
        else
            this.color = 0;
        this.currentColor = this.colors[this.color];
    }

    /**
     * Clone the animation
     *
     * @return A clone of this animation
     */
    @Override
    public AnimationRainbow clone() {
        return new AnimationRainbow(this.text.clone(), this.options.clone());
    }

    private String stripColors(String text) {
        return text.replaceAll(StringUtils.colorPattern, "");
    }
}
