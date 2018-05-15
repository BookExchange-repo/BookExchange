package ee.ttu.BookExchange.utilities;

public class SizeHelper {
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
}
