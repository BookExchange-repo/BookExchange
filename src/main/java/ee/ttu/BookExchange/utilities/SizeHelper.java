package ee.ttu.BookExchange.utilities;

public class SizeHelper {
    public static final int ISBN_10_LENGTH = 10;
    public static final int ISBN_13_LENGTH = 13;

    public static String mapMarkerSizeToString(int mapMarkerSize) {
        if (mapMarkerSize >= 1 && mapMarkerSize <= 5)
            return "small";
        else if (mapMarkerSize >= 6 && mapMarkerSize <= 15)
            return "medium";
        else if (mapMarkerSize >= 16)
            return "big";
        else
            return "none";
    }

    public static boolean isISBNString(String input) {
        input = input.trim();
        int length = input.length();
        if (length != ISBN_10_LENGTH && length != ISBN_13_LENGTH)
            return false;

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(input.charAt(i)))
                return false;
        }
        return true;
    }
}
