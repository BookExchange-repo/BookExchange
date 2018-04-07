package ee.ttu.BookExchange.utilities;

import java.security.SecureRandom;

public class Random {
    private static final String RAND_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

    public static String genRandomString(int stringLength, boolean useSpecial) {
        String sessionKey = "";
        java.util.Random random = new SecureRandom();
        for (int i = 0; i < stringLength; i++) {
            sessionKey += RAND_CHARS.charAt(
                    random.nextInt((useSpecial) ? RAND_CHARS.length() : RAND_CHARS.length() - 2));
        }
        return sessionKey;
    }
}
