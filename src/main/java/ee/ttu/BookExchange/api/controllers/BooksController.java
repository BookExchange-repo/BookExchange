package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.services.BooksService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    private String getStringOrEmptyString(String input) {
        return (input == null) ? "" : input;
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public Map<String, List<Books>> getAllBooks(
            @RequestParam(value = "filter") Optional<String> filterMask,
            @RequestParam(value = "filterparam") Optional<String> filterParam,
            @RequestParam(value = "sort") Optional<String> sortMask,
            @RequestParam(value = "sortdesc") Optional<Boolean> isSortDesc)
    {
        if (!isSortDesc.isPresent())
            isSortDesc = Optional.of(false);
        Map<String, List<Books>> map = new HashMap<>();
        List<Books> allBooks = booksService.getAllBooks();
        if (filterMask.isPresent() && filterParam.isPresent()) {
            allBooks = allBooks.stream()
                    .filter(e -> {
                        switch (filterMask.get()) {
                            case "id":
                                return Integer.toString(e.getId()).equals(filterParam.get());
                            case "title":
                                return e.getTitle().equals(filterParam.get());
                            case "author":
                                return e.getAuthor() != null && e.getAuthor().equals(filterParam.get());
                            case "description":
                                return e.getDescription().equals(filterParam.get());
                            case "conditiondesc":
                                return e.getConditiondesc() != null && e.getConditiondesc().equals(filterParam.get());
                            case "price":
                                return e.getPrice().toString().equals(filterParam.get());
                            case "likes":
                                return Integer.toString(e.getLikes()).equals(filterParam.get());
                            case "isbn":
                                return e.getIsbn() != null && e.getIsbn().equals(filterParam.get());
                            case "imagepath":
                                return e.getImagepath().equals(filterParam.get());
                            case "publisher":
                                return e.getPublisher() != null && e.getPublisher().equals(filterParam.get());
                            case "pubyear":
                                return e.getPubyear() != null && e.getPubyear().equals(filterParam.get());
                            case "language":
                                return e.getLanguage() != null && e.getLanguage().equals(filterParam.get());
                            case "postdate":
                                return e.getPostdate().toString().equals(filterParam.get());
                            case "userid":
                                return e.getUserid().equals(filterParam.get());
                            case "genreid":
                                return e.getGenreid() != null && e.getGenreid().equals(filterParam.get());
                            case "city":
                                return e.getCity() != null && e.getCity().equals(filterParam.get());
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        }
        if (sortMask.isPresent()) {
            allBooks = allBooks.stream()
                    .sorted((e1, e2) -> {
                        switch (sortMask.get()) {
                            case "id":
                                return Integer.compare(e1.getId(), e2.getId());
                            case "title":
                                return e1.getTitle().compareTo(e2.getTitle());
                            case "author":
                                return getStringOrEmptyString(e1.getAuthor())
                                        .compareTo(getStringOrEmptyString(e2.getAuthor()));
                            case "description":
                                return e1.getDescription().compareTo(e2.getDescription());
                            case "conditiondesc":
                                return getStringOrEmptyString(e1.getConditiondesc())
                                        .compareTo(getStringOrEmptyString(e2.getConditiondesc()));
                            case "price":
                                return e1.getPrice().toString().compareTo(e2.getPrice().toString());
                            case "likes":
                                return Integer.compare(e1.getLikes(), e2.getLikes());
                            case "isbn":
                                return getStringOrEmptyString(e1.getIsbn())
                                        .compareTo(getStringOrEmptyString(e2.getIsbn()));
                            case "imagepath":
                                return e1.getImagepath().compareTo(e2.getImagepath());
                            case "publisher":
                                return getStringOrEmptyString(e1.getPublisher())
                                        .compareTo(getStringOrEmptyString(e2.getPublisher()));
                            case "pubyear":
                                return getStringOrEmptyString(e1.getPubyear())
                                        .compareTo(getStringOrEmptyString(e2.getPubyear()));
                            case "language":
                                return getStringOrEmptyString(e1.getLanguage())
                                        .compareTo(getStringOrEmptyString(e2.getLanguage()));
                            case "postdate":
                                return e1.getPostdate().toString().compareTo(e2.getPostdate().toString());
                            case "userid":
                                return e1.getUserid().compareTo(e2.getUserid());
                            case "genreid":
                                return getStringOrEmptyString(e1.getGenreid())
                                        .compareTo(getStringOrEmptyString(e2.getGenreid()));
                            case "city":
                                return getStringOrEmptyString(e1.getCity())
                                        .compareTo(getStringOrEmptyString(e2.getCity()));
                            default:
                                return 0;
                        }
                    })
                    .collect(Collectors.toList());
        }
        if (isSortDesc.get())
            Collections.reverse(allBooks);
        map.put("books", allBooks);
        map.put("errors", new ArrayList<>());
        return map;
        //return booksService.getAllBooks();
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public Books getBook(@RequestParam(value = "id") int bookId) {
        return booksService.getBookById(bookId);
    }
}
