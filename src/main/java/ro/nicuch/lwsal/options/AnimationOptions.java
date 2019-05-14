package ro.nicuch.lwsal.options;

public class AnimationOptions implements Cloneable {
    private int update_time = 20, space_between = 5, display_size = 10;
    private boolean strip_colors = false;

    public AnimationOptions setOptionIntEnum(OptionIntEnum type, int value) {
        switch (type) {
            case UPDATE_TIME:
                this.update_time = value;
                break;
            case DISPLAY_SIZE:
                this.display_size = value;
                break;
            case SPACE_BETWEEN:
                this.space_between = value;
        }
        return this;
    }

    public int getOptionInt(OptionIntEnum type) {
        switch (type) {
            case UPDATE_TIME:
                return this.update_time;
            case DISPLAY_SIZE:
                return this.display_size;
            case SPACE_BETWEEN:
                return this.space_between;
        }
        return 0;
    }

    public AnimationOptions setOptionBooleanEnum(OptionBooleanEnum type, boolean value) {
        switch (type) {
            case STRIP_COLORS:
                this.strip_colors = value;
        }
        return this;
    }

    public boolean getOptionBoolean(OptionBooleanEnum type) {
        switch (type) {
            case STRIP_COLORS:
                return this.strip_colors;
        }
        return false;
    }

    public enum OptionIntEnum {
        UPDATE_TIME, SPACE_BETWEEN, DISPLAY_SIZE
    }

    public enum OptionBooleanEnum {
        STRIP_COLORS
    }

    @Override
    public AnimationOptions clone() {
        AnimationOptions options = new AnimationOptions();
        options.setOptionIntEnum(OptionIntEnum.UPDATE_TIME, this.update_time);
        options.setOptionIntEnum(OptionIntEnum.DISPLAY_SIZE, this.display_size);
        options.setOptionIntEnum(OptionIntEnum.SPACE_BETWEEN, this.space_between);
        options.setOptionBooleanEnum(OptionBooleanEnum.STRIP_COLORS, this.strip_colors);
        return options;
    }
}
