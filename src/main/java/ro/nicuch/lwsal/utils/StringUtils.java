package ro.nicuch.lwsal.utils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String splitterPattern = "((?:&[0123456789abcdefiklmno])+)(?:(.+?)(?=(?:&[0123456789abcdefiklmno])|$)|$)";
    public static final String colorPattern = "((?:&[0123456789abcdefiklmno])+)";

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

    public static LinkedList<ColoredText> splitByColors(String string) {
        //create a new list
        LinkedList<ColoredText> list = new LinkedList<>();
        //match any color & text
        //An exemple & test for how the matcher work: https://regex101.com/r/89lnwo/1/
        //Also, the pattern will be case insensitive
        Matcher matcher = Pattern.compile(splitterPattern, Pattern.CASE_INSENSITIVE).matcher(string);
        //Does it find any?
        while (matcher.find())
            //Add all the colors to list
            //The last group (2) can be null
            list.addLast(new ColoredText(matcher.start(), matcher.group(1), (matcher.group(2) == null) ? "" : matcher.group(2)));
        //return the list
        return list;
    }
}
