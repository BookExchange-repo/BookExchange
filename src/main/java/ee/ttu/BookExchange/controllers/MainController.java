package ee.ttu.BookExchange.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @RequestMapping(value = "/")
    public String index() {
        return "<h3>Welcome to the home page!</h3>";
    }
}
