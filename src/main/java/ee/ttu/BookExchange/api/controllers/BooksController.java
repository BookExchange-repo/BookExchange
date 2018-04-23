package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.services.BooksService;
import ee.ttu.BookExchange.api.services.StatusService;
import ee.ttu.BookExchange.api.services.WatchlistService;
import ee.ttu.BookExchange.exceptions.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/books", produces = "application/json")
public class BooksController {
    private BooksService booksService;
    private StatusService statusService;
    private WatchlistService watchlistService;

    public BooksController(BooksService booksService,
                           StatusService statusService,
                           WatchlistService watchlistService)
    {
        this.booksService = booksService;
        this.statusService = statusService;
        this.watchlistService = watchlistService;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    Map<String, Object> addBook(@RequestBody Books inputBook) throws APIException {
        inputBook.setStatus(statusService.getStatusByIdEst(1));
        if (inputBook.getDescription() == null || inputBook.getDescription().isEmpty())
            throw new APIException("CANNOT_BOOKS_ADD");
        booksService.saveBook(inputBook);
        Map<String, Object> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();
        result.put("id", inputBook.getId());
        result.put("errors", allErrors);
        return result;
    }

    private String[] parseArrayString(String arrayString) {
        String[] output = {};
        if (arrayString.charAt(0) == '[' && arrayString.charAt(arrayString.length() - 1) == ']') {
            output = arrayString.substring(1, arrayString.length() - 1).split(Pattern.quote(","));
        } else {
            output = new String[1];
            output[0] = arrayString;
        }
        return output;
    }

    private String getStringOrEmptyString(String input) {
        return (input == null) ? "" : input;
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public Map<String, List<Books>> getAllBooks(
            @RequestParam Map<String,String> requestParams,
            @RequestParam(value = "sort") Optional<String> sortMask,
            @RequestParam(value = "sortdesc") Optional<Boolean> isSortDesc,
            @RequestParam(value = "session") Optional<String> session) throws APIException
    {
        int paramsSize = 0;
        boolean blockSensitive = true;
        if (sortMask.isPresent())
            paramsSize++;
        if (isSortDesc.isPresent())
            paramsSize++;
        if (session.isPresent()) {
            paramsSize++;
            if (UsersController.getUserIdBySession(session.get()).isPresent())
                blockSensitive = false;
            else
                throw new APIException("CANNOT_BOOKS_GETALL");
        }

        if (!isSortDesc.isPresent())
            isSortDesc = Optional.of(false);
        Map<String, List<Books>> outputMap = new HashMap<>();
        List<Books> allBooks = booksService.getAllBooks();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (entry.getValue().isEmpty())
                continue;
            String[] valueArray = parseArrayString(entry.getValue());
            if (entry.getKey().equals("sort") || entry.getKey().equals("sortdesc"))
                continue;
            if (valueArray.length == 1 && valueArray[0].isEmpty())
                continue;

            List<Books> filteredBooks;
            if (requestParams.size() == paramsSize)
                filteredBooks = allBooks;
            else
                filteredBooks = new ArrayList<>();
            for (String value : valueArray) {
                filteredBooks.addAll(allBooks.stream()
                        .filter(e -> {
                            switch (entry.getKey()) {
                                case "id":
                                    return Integer.toString(e.getId()).equals(value);
                                case "title":
                                    return e.getTitle().equals(value);
                                case "author":
                                    return e.getAuthor() != null && e.getAuthor().equals(value);
                                case "description":
                                    return e.getDescription().equals(value);
                                case "conditiondesc":
                                    return Integer.toString(e.getConditiondesc().getId()).equals(value);
                                case "price":
                                    return e.getPrice().toString().equals(value);
                                case "likes":
                                    return Integer.toString(e.getLikes()).equals(value);
                                case "isbn":
                                    return e.getIsbn() != null && e.getIsbn().equals(value);
                                case "imagepath":
                                    return e.getImagepath().equals(value);
                                case "publisher":
                                    return e.getPublisher() != null && e.getPublisher().equals(value);
                                case "pubyear":
                                    return e.getPubyear() != null && e.getPubyear().equals(value);
                                case "language":
                                    return Integer.toString(e.getLanguage().getId()).equals(value);
                                case "postdate":
                                    return e.getPostdate().toString().equals(value);
                                case "userid":
                                    return Integer.toString(e.getUserid().getId()).equals(value);
                                case "genreid":
                                    return Integer.toString(e.getGenreid().getId()).equals(value);
                                case "city":
                                    return Integer.toString(e.getCity().getId()).equals(value);
                                case "search":
                                    return e.getTitle().toLowerCase().contains(value.toLowerCase());
                                default:
                                    return false;
                            }
                        })
                        .collect(Collectors.toList()));
            }
            allBooks = filteredBooks;
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
                                return Integer.compare(e1.getConditiondesc().getId(), e2.getConditiondesc().getId());
                            case "price":
                                return e1.getPrice().compareTo(e2.getPrice());
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
                                return Integer.compare(e1.getLanguage().getId(), e2.getLanguage().getId());
                            case "postdate":
                                return e1.getPostdate().compareTo(e2.getPostdate());
                            case "userid":
                                return Integer.compare(e1.getUserid().getId(), e2.getUserid().getId());
                            case "genreid":
                                return Integer.compare(e1.getGenreid().getId(), e2.getGenreid().getId());
                            case "city":
                                return Integer.compare(e1.getCity().getId(), e2.getCity().getId());
                            case "watchlist":
                                return Long.compare(watchlistService.findAmountByBookId(e1.getId()),
                                    watchlistService.findAmountByBookId(e2.getId()));
                            default:
                                return 0;
                        }
                    })
                    .collect(Collectors.toList());
        }
        if (isSortDesc.get())
            Collections.reverse(allBooks);
        for (Books book : allBooks) {
            if (blockSensitive) {
                book.getUserid().setEmail("");
                book.getUserid().setPass_hash("");
                book.getUserid().setPass_salt("");
                book.getUserid().setPhone("");
            }
            book.setAmountOfAdds(watchlistService.findAmountByBookId(book.getId()));
        }
        allBooks = allBooks.stream()
                .filter(e -> e.getStatus().getId() == 1)
                .collect(Collectors.toList());
        outputMap.put("books", allBooks);
        outputMap.put("errors", new ArrayList<>());
        return outputMap;
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public Books getBook(@RequestParam(value = "id") int bookId,
                         @RequestParam(value = "session") Optional<String> session) throws APIException
    {
        boolean blockSensitive = true;
        if (session.isPresent()) {
            if (UsersController.getUserIdBySession(session.get()).isPresent())
                blockSensitive = false;
            else
                throw new APIException("CANNOT_BOOKS_GETINFOID");
        }
        Books book = booksService.getBookById(bookId);
        if (blockSensitive) {
            book.getUserid().setEmail("");
            book.getUserid().setPass_hash("");
            book.getUserid().setPass_salt("");
            book.getUserid().setPhone("");
        }
        book.setAmountOfAdds(watchlistService.findAmountByBookId(book.getId()));
        return book;
    }

    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Map<String, List<String>> apiErrorHandler(APIException e) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();
        allErrors.add(e.getMessage());
        result.put("errors", allErrors);
        return result;
    }
}
