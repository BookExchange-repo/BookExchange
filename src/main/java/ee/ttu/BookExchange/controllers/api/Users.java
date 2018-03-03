package ee.ttu.BookExchange.controllers.api;

import ee.ttu.BookExchange.Application;
import ee.ttu.BookExchange.utilities.Checksum;
import ee.ttu.BookExchange.utilities.SQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class Users {
    private static final String RAND_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";
    private static final int SESS_KEY_LENGTH = 64;
    private static HashMap<Integer, String> allSessions = new HashMap<>();

    static Optional<Integer> getUserIdBySession(String session) {
        Optional<Integer> result = Optional.empty();
        try {
            if (!session.isEmpty()) {
                Integer found = -1;
                for (Map.Entry<Integer, String> value : allSessions.entrySet()) {
                    if (value.getValue().equals(session))
                        found = value.getKey();
                }
                if (found >= 0) {
                    result = Optional.of(found);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public String getAllUsers() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            String[] values = {"id", "username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
            SQL sql = SQL.queryAllFromTable("users", values);
            values[4] = "regdate";

            JSONArray userArray = new JSONArray();
            for (int i = 0; i < sql.getQueryRows(); i++) {
                JSONObject userObject = new JSONObject();
                for (int j = 0; j < values.length; j++) {
                    userObject.put(values[j], sql.getQueryCell(i, j));
                }
                userArray.add(userObject);
            }
            jsonObject.put("users", userArray);
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_GETALL");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public String getUserInfoById(@RequestParam(value = "id") String idString) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            int userId = Integer.parseInt(idString);
            String[] values = {"id", "username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
            SQL sql = SQL.queryAllFromTable("users", values);
            values[4] = "regdate";

            if (userId >= sql.getQueryRows())
                throw new RuntimeException();

            for (int i = 0; i < values.length; i++) {
                jsonObject.put(values[i], sql.getQueryCell(userId, i));
            }
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_GETINFOID");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.GET)
    public String getUserInfo(@CookieValue(value = "session", defaultValue = "") String cookie) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            Optional<Integer> found = getUserIdBySession(cookie);
            if (!found.isPresent())
                throw new RuntimeException();
            else {
                String[] values = {"username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
                SQL sql = new SQL();
                sql.executeQuery("use " + Application.databaseName + ";");
                sql.executeQuery(
                        "SELECT " + sql.escapeString(values) + " FROM users WHERE id = '" + found.get() + "';");
                values[3] = "regdate";
                sql.printQueryResults();

                for (int i = 0; i < values.length; i++) {
                    jsonObject.put(values[i], sql.getQueryCell(0, i));
                }
            }
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_GETINFO");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String createUser(@RequestParam(value = "user") String userName,
                             @RequestParam(value = "fullname") String fullName,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "hash") String passHash,
                             @RequestParam(value = "salt") String passSalt)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            SQL sql = new SQL();

            if (userName.isEmpty() || sql.escapeString(userName).length() > 30)
                jsonErrors.add("FAIL_LENGTH_USERNAME");
            if (fullName.isEmpty() || sql.escapeString(fullName).length() > 126)
                jsonErrors.add("FAIL_LENGTH_FULLNAME");
            if (email.isEmpty() || sql.escapeString(email).length() > 62)
                jsonErrors.add("FAIL_LENGTH_EMAIL");
            if (passHash.isEmpty() || sql.escapeString(passHash).length() != 64)
                jsonErrors.add("FAIL_LENGTH_PASSHASH");
            if (passSalt.isEmpty() || sql.escapeString(passSalt).length() != 10)
                jsonErrors.add("FAIL_LENGTH_PASSSALT");

            if (jsonErrors.size() == 0) {
                sql.executeQuery("use " + Application.databaseName + ";");
                sql.executeQuery("SELECT username FROM users WHERE username = '" + sql.escapeString(userName) + "';");
                sql.printQueryResults();
                if (sql.getQueryRows() != 0) {
                    jsonErrors.add("FAIL_EXISTS_USERNAME");
                }
                sql.executeQuery("SELECT email FROM users WHERE email = '" + sql.escapeString(email) + "';");
                sql.printQueryResults();
                if (sql.getQueryRows() != 0) {
                    jsonErrors.add("FAIL_EXISTS_EMAIL");
                }

                if (jsonErrors.size() == 0) {
                    sql.executeQuery("INSERT INTO users (" +
                            sql.escapeString("username", "email", "full_name", "pass_hash", "pass_salt") +
                            ") VALUES (" +
                            sql.escapeString(
                                    true, userName, email, fullName, passHash, passSalt) + ");");
                    sql.printQueryResults();
                }
            }
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_REGISTER");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginUser(@RequestParam(value = "user") String userName,
                            @RequestParam(value = "pass") String password,
                            HttpServletResponse response)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            SQL sql = new SQL();
            sql.executeQuery("use " + Application.databaseName + ";");
            sql.executeQuery("SELECT username, pass_salt, pass_hash, id " +
                    "FROM users WHERE username = '" + sql.escapeString(userName) + "';");
            sql.printQueryResults();
            if (sql.getQueryRows() == 0)
                throw new RuntimeException();

            if (Checksum.calculateSHA256(sql.getQueryCell(0, 1), password)
                    .equals(sql.getQueryCell(0, 2)))
            {
                int userId = Integer.parseInt(sql.getQueryCell(0, 3));
                String sessionKey = allSessions.get(userId);
                if (sessionKey == null) {
                    sessionKey = "";
                    Random random = new SecureRandom();
                    for (int i = 0; i < SESS_KEY_LENGTH; i++) {
                        sessionKey += RAND_CHARS.charAt(random.nextInt(RAND_CHARS.length()));
                    }
                    allSessions.put(userId, sessionKey);
                }
                jsonObject.put("session", sessionKey);
                response.addCookie(new Cookie("session", sessionKey));
            } else
                throw new RuntimeException();
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_LOGIN");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logoutUser(@CookieValue(value = "session", defaultValue = "") String cookie) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            getUserIdBySession(cookie).ifPresent(i -> allSessions.remove(i));
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_LOGOUT");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }
}
