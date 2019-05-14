package ro.nicuch.lwsal.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.utils.ColoredText;
import ro.nicuch.lwsal.utils.StringUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationScroller extends Animation {
    private Animation text;
    private AnimationOptions options = new AnimationOptions();
    private int nextUpdate = 0, position = 0;
    private String spaceBetweenText = "";

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
        return this.getScrolledText(text);
    }

    @Override
    public String getText(Player player) {
        if (this.text == null)
            return "";
        String text = this.text.getText(player);
        if (text == null || text.isEmpty())
            return "";
        return this.getScrolledText(text);
    }

    //The main operations for scrolled text
    private String getScrolledText(String text) {
        int display_size = this.options.getOptionInt(AnimationOptions.OptionIntEnum.DISPLAY_SIZE);
        StringBuilder ct = new StringBuilder(text);

        ct.append(this.spaceBetweenText);

        if (!StringUtils.stringStartWith(ct.toString(), Pattern.compile(StringUtils.colorPattern)))
            ct.insert(0, "&r");

        String it = ct.toString().replaceAll(StringUtils.colorPattern, "");
        int itl = it.length();
        while (itl < display_size) {
            ct.append(ct.toString());
            it = ct.toString().replaceAll(StringUtils.colorPattern, "");
            itl = it.length();
        }
        ct.append(ct.toString());
        it = ct.toString().replaceAll(StringUtils.colorPattern, "");
        itl = it.length();

        LinkedList<ColoredText> colors = StringUtils.splitByColors(ct.toString());

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

    private void updateTimer() {
        if (this.options == null) {
            this.options = new AnimationOptions();
        }
        int update_time = this.options.getOptionInt(AnimationOptions.OptionIntEnum.UPDATE_TIME);
        if (update_time > 1) {
            if (this.nextUpdate < update_time - 1) {
                this.nextUpdate++;
                return;
            }
            this.nextUpdate = 0;
        }
    }

    @Override
    public void update() {
        if (this.text == null) {
            this.updateTimer();
            return;
        }
        /*update timer start*/
        this.text.update(); //Update the text first.
        String text = this.text.getText();

        //If the text is null or empty, the update_time will still be updated
        //The position however will be set to 0
        if (text == null || text.isEmpty()) {
            this.updateTimer();
            return;
        }

        int display_size = this.options.getOptionInt(AnimationOptions.OptionIntEnum.DISPLAY_SIZE);
        StringBuilder ct = new StringBuilder(text);

        ct.append(this.spaceBetweenText);

        if (!StringUtils.stringStartWith(ct.toString(), Pattern.compile(StringUtils.colorPattern)))
            ct.insert(0, "&r");

        String it = ct.toString().replaceAll(StringUtils.colorPattern, "");
        int itl = it.length();
        while (itl < display_size) {
            ct.append(ct.toString());
            it = ct.toString().replaceAll(StringUtils.colorPattern, "");
            itl = it.length();
        }
        ct.append(ct.toString());
        it = ct.toString().replaceAll(StringUtils.colorPattern, "");
        itl = it.length();

        boolean positionUpdatedOnce = false;
        if (position > itl) {
            position = 0;
            positionUpdatedOnce = true;
        }
        this.updateTimer();
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
        return new AnimationScroller(this.text.clone(), this.options.clone());
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
}
