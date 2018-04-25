package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.utilities.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClassifierControllerIntegrationTest {
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddBook() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONParser jsonParser = new JSONParser();
            String loginPath = "/api/users/login?email=email@email.com&pass=testtest";
            String addPath = "/api/books/add?session=";
            String getBookPath = "/api/books/getinfoid?id=";
            String output = this.restTemplate.getForObject(
                    "http://localhost:" + serverPort + loginPath, String.class);
            JSONObject jsonObj = (JSONObject) jsonParser.parse(output);
            String session = (String) jsonObj.get("session");
            if (session.isEmpty())
                throw new RuntimeException();

            String newBookTitle = Random.genRandomString(20, false);
            String newBookDesc = Random.genRandomString(50, false);
            String jsonToSend = "{\"imagepath\":\"https://bookmarket.online:18000/images/no-image.svg\"," +
                    "\"title\":\"" + newBookTitle + "\",\"price\":\"1\",\"description\":" +
                    "\"" + newBookDesc + "\",\"conditiondesc\":{\"id\":1},\"language\":" +
                    "{\"id\":1},\"genreid\":{\"id\":1},\"city\":{\"id\":1}}";
            HttpEntity<String> request = new HttpEntity<>(jsonToSend, headers);
            output = this.restTemplate.postForObject(
                    "http://localhost:" + serverPort + addPath + session, request, String.class);
            jsonObj = (JSONObject) jsonParser.parse(output);
            Long newBookId = (Long) jsonObj.get("id");

            Books bookOutput = this.restTemplate.getForObject(
                    "http://localhost:" + serverPort + getBookPath + newBookId, Books.class);
            assertEquals(newBookTitle, bookOutput.getTitle());
            assertEquals(newBookDesc, bookOutput.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }
}
