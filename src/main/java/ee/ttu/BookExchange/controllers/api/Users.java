package ee.ttu.BookExchange.controllers.api;

import ee.ttu.BookExchange.Application;
import ee.ttu.BookExchange.utilities.SQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class Users {
    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public String getAllUsers() {
        JSONObject jsonObject = new JSONObject();
        try {
            SQL sql = new SQL("localhost", "root", "toor");
            sql.executeQuery("use " + Application.databaseName + ";");

            String[] values = {"username", "email", "full_name", "UNIX_TIMESTAMP(regdate)", "isverified"};
            sql.executeQuery("SELECT " + sql.escapeString(values) + " FROM users;");
            sql.printQueryResults();
            values[3] = "regdate";

            JSONArray userArray = new JSONArray();
            for (int i = 0; i < sql.getQueryRows(); i++) {
                JSONObject userObject = new JSONObject();
                for (int j = 0; j < values.length; j++) {
                    userObject.put(values[j], sql.getQueryCell(i, j));
                }
                userArray.add(userObject);
            }
            jsonObject.put("users", userArray);
        } catch (RuntimeException e) {
            jsonObject.clear();
            jsonObject.put("error", "Cannot get all users.");
        }

        return jsonObject.toJSONString();
    }
}
