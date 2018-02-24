package ee.ttu.BookExchange.controllers.api;

import ee.ttu.BookExchange.Application;
import ee.ttu.BookExchange.utilities.SQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class Users {
    private SQL queryAllUsers(String[] values) {
        SQL sql = new SQL();
        sql.executeQuery("use " + Application.databaseName + ";");
        sql.executeQuery("SELECT " + sql.escapeString(values) + " FROM users;");
        sql.printQueryResults();
        return sql;
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public String getAllUsers() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            String[] values = {"id", "username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
            SQL sql = queryAllUsers(values);
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

    @RequestMapping(value = "getbyid", method = RequestMethod.GET)
    public String getUserById(@RequestParam(value = "id") String idString) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            int userId = Integer.parseInt(idString);
            String[] values = {"id", "username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
            SQL sql = queryAllUsers(values);
            values[4] = "regdate";

            if (userId >= sql.getQueryRows())
                throw new RuntimeException();

            for (int i = 0; i < values.length; i++) {
                jsonObject.put(values[i], sql.getQueryCell(userId, i));
            }
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_USERS_GETBYID");
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
}
