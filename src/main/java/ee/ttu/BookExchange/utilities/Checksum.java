package ee.ttu.BookExchange.utilities;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {
    public static String calculateSHA256(String salt, String pass) {
        String checksum = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            checksum = DatatypeConverter.printHexBinary(
                    messageDigest.digest((salt+pass).getBytes(StandardCharsets.UTF_8))).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        return checksum;
    }
}
