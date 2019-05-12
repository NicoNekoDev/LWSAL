package ro.nicuch.lwsal.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;
import ro.nicuch.lwsal.options.AnimationOptions;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationReverse extends Animation {
    private Animation text = new AnimationText();
    private AnimationOptions options = new AnimationOptions();
    private final String coloredPatten = "(&[0123456789abcdefklmno])+";

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
        LinkedList<ColoredText> colors = this.trimColors(text, text.split(coloredPatten));
        String it = text.replaceAll(coloredPatten, "");
        StringBuilder reversed = new StringBuilder(this.options.getOptionBoolean(AnimationOptions.OptionBooleanEnum.STRIP_COLORS) ? this.stripColors(it) : it)
                .reverse();
        int[] u = getU(colors);
        for (int i = u.length - 1; i >= 0; i--)
            reversed.insert(u[i], colors.get(i).getColorCode());
        return reversed.toString();
    }

    @Override
    public String getText(Player player) {
        String text = this.text.getText(player);
        if (text == null || text.isEmpty())
            return "";
        LinkedList<ColoredText> colors = this.trimColors(text, text.split(coloredPatten));
        String it = text.replaceAll(coloredPatten, "");
        StringBuilder reversed = new StringBuilder(this.options.getOptionBoolean(AnimationOptions.OptionBooleanEnum.STRIP_COLORS) ? this.stripColors(it) : it)
                .reverse();
        int[] u = getU(colors);
        for (int i = u.length - 1; i >= 0; i--)
            reversed.insert(u[i], colors.get(i).getColorCode());
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
        return new AnimationReverse().setText(this.text.clone());
    }

    private String stripColors(String text) {
        return text.replaceAll(this.coloredPatten, "");
    }

    private LinkedList<AnimationReverse.ColoredText> trimColors(String string, String[] splits) {
        LinkedList<AnimationReverse.ColoredText> list = new LinkedList<>();
        int i = 0;
        if (splits[0] == null || splits[0].isEmpty())
            i = 1;
        Matcher matcher = Pattern.compile(this.coloredPatten).matcher(string);
        while (matcher.find()) {
            AnimationReverse.ColoredText ct = new AnimationReverse.ColoredText(matcher.start(), matcher.group(0), splits[i].length());
            list.addLast(ct);
            i++;
        }
        return list;
    }

    public int[] getU(LinkedList<ColoredText> colors) {
        int listSize = colors.size();
        int[] i = new int[listSize];
        int u = 0;
        i[listSize - 1] = 0;

        for (int n = listSize - 2; n >= 0; n--) {
            u += colors.get(n + 1).getTotalLength();
            i[n] = u;
        }
        return i;
    }

    private class ColoredText {
        private final int position;
        private final String colorCode;
        private final int textLength;

        public ColoredText(int position, String colorCode, int textLength) {
            this.position = position;
            this.colorCode = colorCode;
            this.textLength = textLength;
        }

        public int getPosition() {
            return this.position;
        }

        public String getColorCode() {
            return this.colorCode;
        }

        public int getColorCodeLength() {
            return this.colorCode.length();
        }

        public int getTextLength() {
            return this.textLength;
        }

        public int getTotalLength() {
            return this.getColorCodeLength() + this.getTextLength();
        }

        @Override
        public String toString() {
            return position + "";
        }
    }
}
