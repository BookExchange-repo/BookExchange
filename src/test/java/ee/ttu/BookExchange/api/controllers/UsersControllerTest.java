package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UsersControllerTest {
    @Mock
    private static UsersService usersServiceMock;

    @Mock
    private static BooksService booksServiceMock;

    @Mock
    private static WatchlistService watchlistServiceMock;

    @Mock
    private static CityService cityServiceMock;

    @Mock
    private static StatusService statusServiceMock;

    private static UsersController usersController;

    private static City newYork;
    private static City london;
    private static City tallinn;
    private static List<City> cities;
    private static List<Users> users;

    private static void createCities() {
        cities = new ArrayList<>();
        newYork = new City();
        newYork.setId(1);
        newYork.setString("New York");
        newYork.setCounter(0);
        cities.add(newYork);
        london = new City();
        london.setId(2);
        london.setString("London");
        london.setCounter(0);
        cities.add(london);
        tallinn = new City();
        tallinn.setId(3);
        tallinn.setString("Tallinn");
        tallinn.setCounter(0);
        cities.add(tallinn);
    }

    public static List<Users> createUser(List<Users> input) {
        input = new ArrayList<>();
        Users user = new Users();
        user.setId(1);
        user.setUsername("someuser");
        user.setEmail("some@user.com");
        user.setFull_name("Some One");
        user.setCity(newYork);
        user.setPass_hash("PASSWORD_HASH");
        user.setPass_salt("PASSWORD_SALT");
        user.setIsverified((byte)0);
        user.setRegdate(new Timestamp(968396765));
        user.setPhone("12345678");
        input.add(user);
        return input;
    }

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        createCities();
        users = createUser(users);
        usersServiceMock = mock(UsersService.class);
        booksServiceMock = mock(BooksService.class);
        watchlistServiceMock = mock(WatchlistService.class);
        cityServiceMock = mock(CityService.class);
        statusServiceMock = mock(StatusService.class);
        when(usersServiceMock.getAllUsers()).thenReturn(users);
    }

    @Before
    public void setUpTest() {
        // before each test
        usersController = new UsersController(usersServiceMock, booksServiceMock,
                watchlistServiceMock, cityServiceMock, statusServiceMock);
    }

    @Test
    public void testGetAllUsersOneUser() {
        Map<String, List<Users>> allUsers = usersController.getAllUsers(Optional.of("SESSION"));
        assertTrue(allUsers.containsKey("users"));
        assertEquals(1, allUsers.get("users").size());
        assertEquals("Some One", allUsers.get("users").get(0).getFull_name());
    }
}
