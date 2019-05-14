package ro.nicuch.lwsal.utils;

public class ColoredText {
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
