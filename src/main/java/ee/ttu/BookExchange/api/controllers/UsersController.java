package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.models.StatusEst;
import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.models.Watchlist;
import ee.ttu.BookExchange.api.services.*;
import ee.ttu.BookExchange.exceptions.APIException;
import ee.ttu.BookExchange.utilities.Checksum;
import ee.ttu.BookExchange.utilities.Random;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://bookmarket.online"})
@RequestMapping(value = "/api/users", produces = "application/json")
public class UsersController {
    private static final int SESS_KEY_LENGTH = 64;
    private static HashMap<Integer, String> allSessions = new HashMap<>();
    private UsersService usersService;
    private BooksService booksService;
    private WatchlistService watchlistService;
    private CityService cityService;
    private StatusService statusService;

    public UsersController(UsersService usersService,
                           BooksService booksService,
                           WatchlistService watchlistService,
                           CityService cityService,
                           StatusService statusService)
    {
        this.usersService = usersService;
        this.booksService = booksService;
        this.watchlistService = watchlistService;
        this.cityService = cityService;
        this.statusService = statusService;
    }

    private static String generateSession() {
        return Random.genRandomString(SESS_KEY_LENGTH, true);
    }

    public static String externalGetSession(int userId) {
        String newSession = allSessions.get(userId);
        if (newSession == null)
            newSession = generateSession();
        allSessions.put(userId, newSession);
        return newSession;
    }

    static Optional<Integer> getUserIdBySession(String session) {
        Optional<Integer> result = Optional.empty();
        try {
            if (!session.isEmpty()) {
                Integer found = -1;
                for (Map.Entry<Integer, String> value : allSessions.entrySet()) {
                    if (value.getValue().equals(session))
                        found = value.getKey();
                }
                if (found >= 0) {
                    result = Optional.of(found);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private boolean isUserAModerator(int userId) {
        Users user = usersService.getUserById(userId);
        return user != null && user.getIsverified() != 0;
    }

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public Map<String, List<Users>> getAllUsers(@RequestParam(value = "session") Optional<String> session) {
        Map<String, List<Users>> map = new HashMap<>();
        List<Users> allUsers = usersService.getAllUsers();
        for (Users user : allUsers) {
            user.setEmail("");
            user.setPass_hash("");
            user.setPass_salt("");
            user.setPhone("");
        }
        map.put("users", allUsers);
        map.put("errors", new ArrayList<>());
        return map;
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public Users getUser(@RequestParam(value = "id") int userId) {
        return usersService.getUserById(userId);
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.GET)
    public Users getUser(@RequestParam(value = "session") String session) throws APIException {
        List<String> allErrors = new ArrayList<>();
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new APIException("CANNOT_USERS_GETINFO");
        }
        return usersService.getUserById(userId.get());
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

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public Map<String, Object> loginUser(@RequestParam(value = "email") String email,
                                         @RequestParam(value = "pass") String password)
    {
        Map<String, Object> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();
        Users user = usersService.getUserByEmail(email);
        if (user == null || !Checksum.calculateSHA256(user.getPass_salt(), password).equals(user.getPass_hash())) {
            allErrors.add("CANNOT_USERS_LOGIN");
            result.put("errors", allErrors);
            return result;
        }
        String sessionKey = allSessions.get(user.getId());
        if (sessionKey == null) {
            sessionKey = generateSession();
            allSessions.put(user.getId(), sessionKey);
        }
        result.put("session", sessionKey);
        result.put("firstLogin", user.getFull_name() == null || user.getCity() == null || user.getPhone() == null);
        result.put("errors", allErrors);
        return result;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public Map<String, Object> logoutUser(@RequestParam(value = "session") String session) {
        Map<String, Object> result = new HashMap<>();
        getUserIdBySession(session).ifPresent(i -> allSessions.remove(i));
        result.put("errors", new ArrayList<>());
        return result;
    }

    @RequestMapping(value = "addtowatchlist", method = RequestMethod.GET)
    public Map<String, Object> addToWatchlist(@RequestParam(value = "session") String session,
                                              @RequestParam(value = "bookid") int bookId) throws APIException
    {
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent() || booksService.getBookById(bookId) == null) {
            throw new APIException("CANNOT_USERS_ADDTOWATCHLIST");
        }
        if (watchlistService.isBookInWatchlist(userId.get(), bookId)) {
            throw new APIException("FAIL_EXISTS_BOOKID");
        }
        watchlistService.saveBook(userId.get(), bookId);
        Map<String, Object> result = new HashMap<>();
        result.put("errors", new ArrayList<>());
        return result;
    }

    @RequestMapping(value = "getwatchlist", method = RequestMethod.GET)
    public List<Watchlist> getWatchlist(@RequestParam(value = "session") String session) throws APIException {
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new APIException("CANNOT_USERS_GETWATCHLIST");
        }
        return watchlistService.findByUserId(userId.get());
    }

    @RequestMapping(value = "removefromwatchlist", method = RequestMethod.GET)
    public Map<String, Object> removeFromWatchlist(@RequestParam(value = "session") String session,
                                                   @RequestParam(value = "bookid") int bookId) throws APIException
    {
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new APIException("CANNOT_USERS_REMOVEFROMWATCHLIST");
        }
        for (Watchlist watchlist : watchlistService.findByUserId(userId.get())) {
            if (watchlist.getBookid().getId() == bookId) {
                watchlistService.deleteWatchlistWithBook(watchlist);
                break;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("errors", new ArrayList<>());
        return result;
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public Map<String, Object> updateInfo(@RequestParam(value = "session") String session,
                                          @RequestParam(value = "fullname") String fullName,
                                          @RequestParam(value = "city") String city,
                                          @RequestParam(value = "phone") String phone) throws APIException
    {
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new APIException("CANNOT_USERS_UPDATE");
        }
        Users user = usersService.getUserById(userId.get());
        user.setFull_name(fullName);
        user.setCity(cityService.getCityById(Integer.parseInt(city)));
        user.setPhone(phone);
        usersService.saveUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("errors", new ArrayList<>());
        return map;
    }

    @RequestMapping(value = "setstatus", method = RequestMethod.GET)
    public Map<String, Object> setBookStatus(@RequestParam(value = "session") String session,
                                             @RequestParam(value = "bookid") int bookId,
                                             @RequestParam(value = "status") int statusId) throws APIException
    {
        Optional<Integer> userId = getUserIdBySession(session);
        Books foundBook = booksService.getBookById(bookId);
        StatusEst statusToSet = statusService.getStatusByIdEst(statusId);
        if (!userId.isPresent() || foundBook == null ||
                foundBook.getUserid().getId() != userId.get() || statusToSet == null ||
                foundBook.getStatus().getId() != 1)
        {
            throw new APIException("CANNOT_USERS_ADDTOMYBOOKS");
        }
        foundBook.setStatus(statusToSet);
        booksService.saveBook(foundBook);
        Map<String, Object> result = new HashMap<>();
        result.put("errors", new ArrayList<>());
        return result;
    }

    @RequestMapping(value = "getmybooks", method = RequestMethod.GET)
    public List<Books> getMyBooks(@RequestParam(value = "session") String session) throws APIException {
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new APIException("CANNOT_USERS_GETMYBOOKS");
        }
        List<Books> allBooks = booksService.getAllBooks().stream()
                .filter(e -> e.getUserid().getId() == userId.get())
                .collect(Collectors.toList());
        for (Books book : allBooks) {
            book.setAmountOfAdds(watchlistService.findAmountByBookId(book.getId()));
        }
        return allBooks;
    }
}
