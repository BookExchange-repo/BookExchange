package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.services.BooksService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/books", produces = "application/json")
public class BooksController {
    private BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    HashMap<String, Books> addBook(@RequestBody Books inputBook) {
        inputBook.setUserid("Guest");
        booksService.saveBook(inputBook);
        return new HashMap<>();
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public Map<String, List<Books>> getAllBooks() {
        Map<String, List<Books>> map = new HashMap<>();
        map.put("books", booksService.getAllBooks());
        map.put("errors", new ArrayList<>());
        return map;
        //return booksService.getAllBooks();
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public Books getBook(@RequestParam(value = "id") int bookId) {
        return booksService.getBookById(bookId);
    }
}
