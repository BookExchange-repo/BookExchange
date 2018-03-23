package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.services.BooksService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BooksController {
    private BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public Map<String, List<Books>> getAllBooks() {
        Map<String, List<Books>> map = new HashMap<String, List<Books>>();
        map.put("books", booksService.getAllBooks());
        map.put("errors", new ArrayList<>());
        return map;
        //return booksService.getAllBooks();
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public Books getBook(@PathVariable("id") int bookId) {
        return booksService.getBookById(bookId);
    }
}
