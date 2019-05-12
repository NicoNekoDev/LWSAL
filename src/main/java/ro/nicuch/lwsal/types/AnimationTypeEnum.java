package ro.nicuch.lwsal.types;

public enum AnimationTypeEnum {
    TEXT("TEXT"), COMPOUND("COMPOUND"), RAINBOW("RAINBOW"), SCROLLER("SCROLLER"), REVERSE("REVERSE");

    private final String name;

    AnimationTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String lowerCaseNames() {
        return this.name.toLowerCase();
    }

    public static AnimationTypeEnum fromString(String name) {
        switch (name.toUpperCase()) {
            case "TEXT":
                return TEXT;
            case "RAINBOW":
                return RAINBOW;
            case "SCROLLER":
                return SCROLLER;
            case "REVERSE":
                return REVERSE;
            default:
                throw new IllegalArgumentException();
        }
    }
}
