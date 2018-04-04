package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.services.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/oauth2", produces = "application/json")
public class OAuth2Controller {
    private UsersService usersService;

    public OAuth2Controller(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String getAccessGranted() {
        return "Success";
    }

    @RequestMapping(value = "/api/users/google", method = RequestMethod.GET)
    public Map<String, Object> getSessionFromGoogleUser(@RequestHeader("X-Forwarded-Email") String googleEmail,
                                                        @RequestHeader("X-Forwarded-User") String googleUsername)
    {
        Map<String, Object> result = new HashMap<>();
        Users user = usersService.getUserByEmail(googleEmail);
        if (user == null) {
            user = new Users();
            user.setUsername(googleUsername);
            user.setFull_name(googleUsername);
            user.setEmail(googleEmail);
            user.setPass_hash("GOOGLE");
            user.setPass_salt("GOOGLE");
            user.setIsverified((byte)0);
            usersService.saveUser(user);
            user = usersService.getUserByEmail(googleEmail);
        }
        result.put("session", UsersController.externalGetSession(user.getId()));
        result.put("firstLogin", user.getFull_name() == null || user.getCity() == null || user.getPhone() == null);
        result.put("errors", new ArrayList<>());
        return result;
    }
}
