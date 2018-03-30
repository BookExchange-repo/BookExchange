package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.services.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/internal/api/users", produces = "application/json")
public class OAuth2Controller {
    private UsersService usersService;

    public OAuth2Controller(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "google", method = RequestMethod.GET)
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
        result.put("session", ee.ttu.BookExchange.api.oldcontrollers.Users.externalGetSession(user.getId()));
        result.put("error", new ArrayList<>());
        return result;
    }
}
