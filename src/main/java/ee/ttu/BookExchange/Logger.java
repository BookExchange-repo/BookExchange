package ee.ttu.BookExchange;

public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public enum ErrorLevel {
        ERROR, WARNING
    }

    private static void log(ErrorLevel errorLevel, String message) {
        System.out.format("%s%s:%s %s",
                errorLevel == ErrorLevel.ERROR ? ANSI_RED : ANSI_BLUE,
                errorLevel == ErrorLevel.ERROR ? "ERROR" : "WARNING",
                ANSI_RESET, message);
    }

    public static void logError(String message) {
        log(ErrorLevel.ERROR, message);
    }

    public static void logWarning(String message) {
        log(ErrorLevel.WARNING, message);
    }
}
