package ee.ttu.BookExchange.api.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/image", produces = "application/json")
public class ImageController {
    public ImageController() { }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    Map<String, Object> addBook(@RequestBody byte[] imageData) {
        Map<String, Object> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();
        allErrors.add("CANNOT_IMAGE_UPLOAD");
        result.put("errors", allErrors);
        return result;
    }
}
