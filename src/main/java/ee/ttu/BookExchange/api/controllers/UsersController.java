package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.services.UsersService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {
    private UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Users> getAllUsers() {
        //System.out.println(usersService.getAllUsers().get(0).getCity_string());
        return usersService.getAllUsers();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public Users getUser(@PathVariable("id") int userId) {
        return usersService.getUserById(userId);
    }
}
