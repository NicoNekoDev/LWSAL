package ro.nicuch.lwsal.utils;

public class ColoredText {
    private final int position;
    private final String coloredText, simpleText;

    public ColoredText(int position, String coloredText, String simpleText) {
        this.position = position;
        this.coloredText = coloredText;
        this.simpleText = simpleText;
    }

    public int getPosition() {
        return this.position;
    }

    public String getColoredText() {
        return this.coloredText;
    }

    public int getColoredTextLength() {
        return this.coloredText.length();
    }

    public String getSimpleText() {
        return this.simpleText;
    }

    public int getSimpleTextLength() {
        return this.simpleText.length();
    }

    public int getTotalLength() {
        return this.getColoredTextLength() + this.getSimpleTextLength();
    }

    @Override
    public String toString() {
        return position + "";
    }
}
