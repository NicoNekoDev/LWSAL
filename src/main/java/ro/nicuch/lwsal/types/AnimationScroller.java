package ro.nicuch.lwsal.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.utils.StringUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationScroller extends Animation {
    private Animation text;
    private AnimationOptions options = new AnimationOptions();
    private int nextUpdate = 0, position = 0;
    private String spaceBetweenText = "";
    private final String coloredPatten = "(&[0123456789abcdefklmno])+";

    public AnimationScroller() {
        super(AnimationTypeEnum.SCROLLER);
    }

    public AnimationScroller(Animation text, AnimationOptions options) {
        this();
        this.text = text;
        this.options = options;
    }

    public AnimationScroller setText(Animation text) {
        this.text = text;
        return this;
    }

    @Override
    public String getText() {
        if (this.text == null)
            return "";
        String text = this.text.getText();
        if (text == null || text.isEmpty())
            return "";
        int display_size = this.options.getOptionInt(AnimationOptions.OptionIntEnum.DISPLAY_SIZE);
        StringBuilder ct = new StringBuilder(text);

        ct.append(this.spaceBetweenText);

        if (!StringUtils.stringStartWith(ct.toString(), Pattern.compile(coloredPatten)))
            ct.insert(0, "&r");

        String it = ct.toString().replaceAll(coloredPatten, "");
        int itl = it.length();
        while (itl < display_size) {
            ct.append(ct.toString());
            it = ct.toString().replaceAll(coloredPatten, "");
            itl = it.length();
        }
        ct.append(ct.toString());
        it = ct.toString().replaceAll(coloredPatten, "");
        itl = it.length();

        String[] splits = ct.toString().split(coloredPatten);
        LinkedList<ColoredText> colors = trimColors(ct.toString(), splits);

        String stripedText = (it + it).substring(position, position + display_size);

        int[] r = getR(colors);
        LinkedList<ColoredText> sortedList = getSortedColoredTextList(colors, r);

        StringBuilder builder = new StringBuilder(stripedText);
        if (!colors.isEmpty())
            putColors(colors, sortedList, sortedList.getFirst(), builder, itl, ct.length(), r);
        return builder.toString();
    }

    @Override
    public String getText(Player player) {
        if (this.text == null)
            return "";
        String text = this.text.getText(player);
        if (text == null || text.isEmpty())
            return "";
        int display_size = this.options.getOptionInt(AnimationOptions.OptionIntEnum.DISPLAY_SIZE);
        StringBuilder ct = new StringBuilder(text);

        ct.append(this.spaceBetweenText);

        if (!StringUtils.stringStartWith(ct.toString(), Pattern.compile(coloredPatten)))
            ct.insert(0, "&r");

        String it = ct.toString().replaceAll(coloredPatten, "");
        int itl = it.length();
        while (itl < display_size) {
            ct.append(ct.toString());
            it = ct.toString().replaceAll(coloredPatten, "");
            itl = it.length();
        }
        ct.append(ct.toString());
        it = ct.toString().replaceAll(coloredPatten, "");
        itl = it.length();

        String[] splits = ct.toString().split(coloredPatten);
        LinkedList<ColoredText> colors = trimColors(ct.toString(), splits);

        String stripedText = (it + it).substring(position, position + display_size);

        int[] r = getR(colors);
        LinkedList<ColoredText> sortedList = getSortedColoredTextList(colors, r);

        StringBuilder builder = new StringBuilder(stripedText);
        if (!colors.isEmpty())
            putColors(colors, sortedList, sortedList.getFirst(), builder, itl, ct.length(), r);
        return builder.toString();
    }

    @Override
    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive("scroller"));
        jsonObject.add("text", this.text.getJson());
        return jsonObject;
    }

    @Override
    public void update() {
        if (this.text == null)
            return;
        /*update timer start*/
        this.text.update(); //Update the text first.
        String text = this.text.getText();

        //If the text is null or empty, the update_time will still be updated
        //The position however will be set to 0
        if (text == null || text.isEmpty()) {
            int update_time = this.options.getOptionInt(AnimationOptions.OptionIntEnum.UPDATE_TIME);
            if (update_time > 1) {
                if (this.nextUpdate < update_time - 1) {
                    this.nextUpdate++;
                    return;
                }
                this.nextUpdate = 0;
            }
            this.position = 0;
            return;
        }

        int display_size = this.options.getOptionInt(AnimationOptions.OptionIntEnum.DISPLAY_SIZE);
        StringBuilder ct = new StringBuilder(text);

        ct.append(this.spaceBetweenText);

        if (!StringUtils.stringStartWith(ct.toString(), Pattern.compile(coloredPatten)))
            ct.insert(0, "&r");

        String it = ct.toString().replaceAll(coloredPatten, "");
        int itl = it.length();
        while (itl < display_size) {
            ct.append(ct.toString());
            it = ct.toString().replaceAll(coloredPatten, "");
            itl = it.length();
        }
        ct.append(ct.toString());
        it = ct.toString().replaceAll(coloredPatten, "");
        itl = it.length();

        boolean positionUpdatedOnce = false;
        if (position > itl) {
            position = 0;
            positionUpdatedOnce = true;
        }
        int update_time = this.options.getOptionInt(AnimationOptions.OptionIntEnum.UPDATE_TIME);
        if (update_time > 1) {
            if (this.nextUpdate < update_time - 1) {
                this.nextUpdate++;
                return;
            }
            this.nextUpdate = 0;
        }
        if (!positionUpdatedOnce) {
            position++;
            if (position > itl)
                position = 0;
        }
        /*update timer end*/
    }

    public AnimationOptions getOptions() {
        return this.options;
    }

    public AnimationScroller setOptions(AnimationOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public AnimationScroller clone() {
        return new AnimationScroller().setText(this.text.clone());
    }

    private LinkedList<ColoredText> trimColors(String string, String[] splits) {
        LinkedList<ColoredText> list = new LinkedList<>();
        int i = 0;
        /*
        Because scrollers can't have only one color in the string, useless checker.
        if (splits.length == 1)
            return list;
        */
        if (splits[0] == null || splits[0].isEmpty())
            i = 1;
        Matcher matcher = Pattern.compile(this.coloredPatten).matcher(string);
        while (matcher.find()) {
            ColoredText ct = new ColoredText(matcher.start(), matcher.group(0), splits[i].length());
            list.addLast(ct);
            i++;
        }

        return list;
    }

    private void putColors(LinkedList<ColoredText> list, LinkedList<ColoredText> sortedList, ColoredText coloredText, StringBuilder text, int l, int lc, int[] r) {
        int i = list.indexOf(coloredText);
        int sortedListIndex = sortedList.indexOf(coloredText);
        int cp = coloredText.getPosition();
        int cpl = coloredText.getColorCodeLength();
        int cps = coloredText.getTextLength();
        int w = getW(list, r);
        int c = 50;
        int m = getM(list, i);

        if (i == 0) {
            if (r[i] <= 0) {
                c = lc - position - w - m;
            } else {
                c = 0;
            }
        } else if (i > 0 && i < r.length - 1) {
            if (r[i - 1] > 0) {
                if (r[i] > 0) {
                    c = cp - position - w;
                } else {
                    c = lc + cp - position - w;
                }
            } else {
                if (r[i] > 0) {
                    if (r[i + 1] > 0) {
                        c = 0;
                    } else {
                        c = cp - position - w;
                    }
                } else {
                    c = lc + cp - position - w;
                }
            }
        } else if (i > 0 && i == r.length - 1) {
            if (r[i - 1] > 0) {
                if (r[i] > 0) {
                    c = cp - position - w;
                }
            } else if (r[i - 1] <= 0) {
                if (r[i] > 0) {
                    c = 0;
                }
            }
        }
        if (c <= text.length() && c >= 0)
            text.insert(c, coloredText.getColorCode());
        if (sortedListIndex + 1 >= sortedList.size())
            return;
        putColors(list, sortedList, sortedList.get(sortedListIndex + 1), text, l, lc, r);
    }

    private int[] getR(LinkedList<ColoredText> list) {
        int[] n = new int[list.size()];
        int i = 0;
        for (ColoredText text : list) {
            int index = list.indexOf(text);
            n[index] = (i + text.getTextLength()) - position;
            i += text.getTextLength();
        }
        return n;
    }

    private int getW(LinkedList<ColoredText> list, int[] r) {
        int i = 0;
        for (int n = 0; n < list.size(); n++) {
            if (r[n] <= 0)
                i += list.get(n).getColorCodeLength();
        }
        return i;
    }

    private int getM(LinkedList<ColoredText> list, int index) {
        int i = 0;
        for (int n = 0; n < index; n++) {
            i += list.get(n).getColorCodeLength();
        }
        return i;
    }

    private LinkedList<ColoredText> getSortedColoredTextList(LinkedList<ColoredText> list, int[] r) {
        LinkedList<ColoredText> sortedList = new LinkedList<>();
        LinkedList<ColoredText> endList = new LinkedList<>();
        for (ColoredText text : list) {
            int index = list.indexOf(text);
            if (r[index] > 0)
                sortedList.addLast(text);
            else
                endList.addLast(text);
        }
        sortedList.addAll(endList);
        return sortedList;
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
