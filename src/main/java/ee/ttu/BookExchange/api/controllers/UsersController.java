package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.services.UsersService;
import ee.ttu.BookExchange.utilities.Checksum;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/users", produces = "application/json")
public class UsersController {
    private static final String RAND_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";
    private static final int SESS_KEY_LENGTH = 64;
    private static final int PASS_SALT_LENGTH = 10;
    private static HashMap<Integer, String> allSessions = new HashMap<>();
    private UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    private static String generateSession() {
        String sessionKey = "";
        Random random = new SecureRandom();
        for (int i = 0; i < SESS_KEY_LENGTH; i++) {
            sessionKey += RAND_CHARS.charAt(random.nextInt(RAND_CHARS.length()));
        }
        return sessionKey;
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
    public Map<String, List<Users>> getAllUsers() {
        Map<String, List<Users>> map = new HashMap<>();
        List<Users> allUsers = usersService.getAllUsers();
        map.put("users", allUsers);
        map.put("errors", new ArrayList<>());
        return map;
    }

    @RequestMapping(value = "getinfoid", method = RequestMethod.GET)
    public Users getUser(@RequestParam(value = "id") int userId) {
        return usersService.getUserById(userId);
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.GET)
    public Users getUser(@RequestParam(value = "session") String session) {
        List<String> allErrors = new ArrayList<>();
        Optional<Integer> userId = getUserIdBySession(session);
        if (!userId.isPresent()) {
            throw new RuntimeException("CANNOT_USERS_GETINFO");
        }
        return usersService.getUserById(userId.get());
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
            result.put("error", allErrors);
            return result;
        }
        String sessionKey = allSessions.get(user.getId());
        if (sessionKey == null) {
            sessionKey = generateSession();
            allSessions.put(user.getId(), sessionKey);
        }
        result.put("session", sessionKey);
        result.put("error", allErrors);
        return result;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public Map<String, Object> logoutUser(@RequestParam(value = "session") String session) {
        Map<String, Object> result = new HashMap<>();
        getUserIdBySession(session).ifPresent(i -> allSessions.remove(i));
        result.put("error", new ArrayList<>());
        return result;
    }
}
