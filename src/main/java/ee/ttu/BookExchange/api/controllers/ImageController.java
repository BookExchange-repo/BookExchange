package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.utilities.Random;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/image", produces = "application/json")
public class ImageController {
    public ImageController() { }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    Map<String, Object> uploadImage(@RequestParam("fileToUpload") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();
        String fileExtension = file.getOriginalFilename().split("\\.")[1];

        try {
            if (!fileExtension.equals("png") && !fileExtension.equals("jpg"))
                throw new RuntimeException();

            String fileSystemUploadPath = "/var/www/lighttpd/images/";
            String folderPath = Random.genRandomString(9, false);
            String fileSystemImagePath = folderPath + "/1." + fileExtension;

            Path filePath = Paths.get(fileSystemUploadPath + folderPath);
            Files.createDirectories(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(fileSystemUploadPath + fileSystemImagePath);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();
            result.put("imagepath", "https://bookmarket.online:18000/images/" + fileSystemImagePath);
        } catch (Exception e) {
            allErrors.add("CANNOT_IMAGE_UPLOAD");
            result.put("errors", allErrors);
            return result;
        }
        result.put("errors", allErrors);
        return result;
    }
}
