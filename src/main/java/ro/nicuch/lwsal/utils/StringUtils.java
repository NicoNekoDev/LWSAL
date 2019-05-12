package ro.nicuch.lwsal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean stringStartWith(String arg, Pattern regex, int offset) {
        Matcher matcher = regex.matcher(arg);
        return matcher.find() && matcher.start() == offset;
    }

    public static boolean stringStartWith(String arg, Pattern regex) {
        return stringStartWith(arg, regex, 0);
    }

    public static String makeSpacedString(int spaces) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < spaces; i++)
            space.append(" ");
        return space.toString();
    }
}
