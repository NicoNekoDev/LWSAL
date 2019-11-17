package ro.nicuch.lwsal.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.utils.ColoredText;
import ro.nicuch.lwsal.utils.StringUtils;

import java.util.LinkedList;

public class AnimationReverse extends Animation {
    private Animation text = new AnimationText();
    private AnimationOptions options = new AnimationOptions();

    public AnimationReverse() {
        super(AnimationTypeEnum.REVERSE);
    }

    public AnimationReverse(Animation text, AnimationOptions options) {
        this();
        this.text = text;
        this.options = options;
    }

    public AnimationOptions getOptions() {
        return this.options;
    }

    public AnimationReverse setOptions(AnimationOptions options) {
        this.options = options;
        return this;
    }

    public AnimationReverse setText(Animation text) {
        this.text = text;
        return this;
    }

    @Override
    public String getText() {
        String text = this.text.getText();
        if (text == null || text.isEmpty())
            return "";
        return this.getReversedText(text);
    }

    @Override
    public String getText(Player player) {
        String text = this.text.getText(player);
        if (text == null || text.isEmpty())
            return "";
        return this.getReversedText(text);
    }

    //The main operations for reversed text
    private String getReversedText(String text) {
        //split colors
        LinkedList<ColoredText> colors = StringUtils.splitByColors(text);
        //clear the colors (aka incolored text)
        String it = text.replaceAll(StringUtils.colorPattern, "");
        //reverse the incolored text
        StringBuilder reversed = new StringBuilder(this.options.getOptionBoolean(AnimationOptions.OptionBooleanEnum.STRIP_COLORS) ? this.stripColors(it) : it)
                .reverse();
        //calculate where the colors should be placed after reversing the text
        int[] u = getU(colors);
        //loop through all colors
        for (int i = u.length - 1; i >= 0; i--)
            //place all the colors to the correct possitons
            reversed.insert(u[i], colors.get(i).getColoredText());
        //return the text
        return reversed.toString();
    }

    @Override
    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive("reverse"));
        jsonObject.add("text", this.text.getJson());
        return jsonObject;
    }

    @Override
    public void update() {
        this.text.update();
    }

    @Override
    public AnimationReverse clone() {
        return new AnimationReverse(this.text.clone(), this.options.clone());
    }

    private String stripColors(String text) {
        return text.replaceAll(StringUtils.colorPattern, "");
    }

    public int[] getU(LinkedList<ColoredText> colors) {
        int listSize = colors.size();
        //create an array with the size of the list
        //an array is more efficient than a list
        int[] i = new int[listSize];
        //initialization of possitions
        int u = 0;
        //we skip the last color
        //the last color will allways be first in possiton
        i[listSize - 1] = 0;

        //an reverse loop to calculate each color
        //also skipping the last color (listsize - 2)
        for (int n = listSize - 2; n >= 0; n--) {
            //each color possition will be the lenght of the text ahead
            u += colors.get(n + 1).getTotalLength();
            //setting the color possition to array
            i[n] = u;
        }
        //return of the array
        return i;
    }
}
