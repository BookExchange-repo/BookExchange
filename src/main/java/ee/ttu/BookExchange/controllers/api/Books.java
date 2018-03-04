package ee.ttu.BookExchange.controllers.api;

import ee.ttu.BookExchange.Application;
import ee.ttu.BookExchange.utilities.SQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping(value = "/api/books", produces = "application/json")
public class Books {
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addBook(@CookieValue(value = "session", defaultValue = "") String cookie,
                          @RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            //Optional<Integer> userId = Users.getUserIdBySession(cookie);
            Optional<Integer> userId = Optional.of(1);
            if (!userId.isPresent())
                throw new RuntimeException();
            else {
                String[] fieldArray = {"title", "description", "price", "imagepath"};
                int[] fieldLengthArray = {254, 8190, 9, 510};
                JSONObject request = (JSONObject) new JSONParser().parse(body);
                for (int i = 0; i < fieldArray.length; i++) {
                    if (request.get(fieldArray[i]) == null)
                        jsonErrors.add("FAIL_EXISTS_" + fieldArray[i].toUpperCase());
                    else if (request.get(fieldArray[i]).toString().isEmpty()
                            || request.get(fieldArray[i]).toString().length() > fieldLengthArray[i])
                        jsonErrors.add("FAIL_LENGTH_" + fieldArray[i].toUpperCase());
                }
                if (jsonErrors.size() == 0) {
                    double convertedPrice = Double.parseDouble(request.get("price").toString());
                    SQL sql = new SQL();
                    sql.executeQuery("use " + Application.databaseName + ";");
                    sql.executeQuery("INSERT INTO books (" +
                            sql.escapeString(fieldArray) + ", userid" +
                            ") VALUES (" +
                            sql.escapeString(
                                    true, request.get("title").toString(),
                                    request.get("description").toString(),
                                    String.format("%.02f", convertedPrice),
                                    request.get("imagepath").toString(), userId.get().toString()) + ");");
                    sql.printQueryResults();
                    sql.executeQuery("SELECT LAST_INSERT_ID();");
                    sql.printQueryResults();
                    jsonObject.put("id", sql.getQueryCell(0, 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.clear();
            jsonErrors.add("CANNOT_BOOKS_ADD");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public String getAllBooks() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            String[] values = {"id", "title", "description", "price", "imagepath", "language", "userid",
                    "UNIX_TIMESTAMP(postdate)"};
            SQL sql = SQL.queryAllFromTable("books", values);
            values[7] = "postdate";

            JSONArray userArray = new JSONArray();
            for (int i = 0; i < sql.getQueryRows(); i++) {
                JSONObject userObject = new JSONObject();
                for (int j = 0; j < values.length; j++) {
                    userObject.put(values[j], sql.getQueryCell(i, j));
                }
                // TODO: temporary placeholder for demo
                userObject.put("author", "Author");
                userObject.put("condition", "The item is in good condition.");
                userObject.put("likes", "4");
                userObject.put("isbn", "0123456789012");
                userObject.put("publisher", "BookCompany Ltd.");
                userObject.put("pubyear", "2015");
                userObject.put("genreid", "0");
                userArray.add(userObject);
            }
            jsonObject.put("books", userArray);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.clear();
            jsonErrors.add("CANNOT_BOOKS_GETALL");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public String getBookInfoById(@RequestParam(value = "id") String idString) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonErrors = new JSONArray();
        try {
            int bookId = Integer.parseInt(idString);
            String[] values = {"id", "title", "description", "price", "imagepath", "language", "userid",
                    "UNIX_TIMESTAMP(postdate)"};
            SQL sql = new SQL();
            sql.executeQuery("use " + Application.databaseName + ";");
            sql.executeQuery("SELECT " + sql.escapeString(values) + " FROM books WHERE id=" + bookId + ";");
            values[7] = "postdate";
            sql.printQueryResults();

            if (sql.getQueryRows() == 0)
                throw new RuntimeException();

            for (int i = 0; i < values.length; i++) {
                jsonObject.put(values[i], sql.getQueryCell(0, i));
            }
            // TODO: temporary placeholder for demo
            jsonObject.put("author", "Author");
            jsonObject.put("condition", "The item is in good condition.");
            jsonObject.put("likes", "4");
            jsonObject.put("isbn", "0123456789012");
            jsonObject.put("publisher", "BookCompany Ltd.");
            jsonObject.put("pubyear", "2015");
            jsonObject.put("genreid", "0");
        } catch (Exception e) {
            jsonObject.clear();
            jsonErrors.add("CANNOT_BOOKS_GETINFOID");
        }

        jsonObject.put("errors", jsonErrors);
        return jsonObject.toJSONString();
    }
}
