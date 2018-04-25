package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.BooksService;
import ee.ttu.BookExchange.api.services.CityService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ClassifierControllerTest {
    @Mock
    private static BooksService booksServiceMock;

    @Mock
    private static CityService cityServiceMock;

    private static ClassifierController classifierController;

    private static Books book1;
    private static City newYork;
    private static City london;
    private static City tallinn;

    private static List<City> allCities;
    private static List<Books> allBooks;

    private static void createOneBook() {
        book1 = new Books();
        book1.setId(1);
        book1.setTitle("Good book");
        book1.setAuthor("Author");
        book1.setDescription("This is a description of a book.");
        ConditionEng condition = new ConditionEng();
        condition.setId(1);
        condition.setString("Good condition");
        condition.setCounter(0);
        book1.setConditiondesc(condition);
        book1.setPrice(new BigDecimal("123.45"));
        book1.setLikes(0);
        book1.setIsbn("1234567890123");
        book1.setImagepath("http://example.com/image/path.jpg");
        book1.setPublisher("Publisher Inc.");
        book1.setPubyear("2001");
        LanguageEng language = new LanguageEng();
        language.setId(1);
        language.setString("English");
        language.setCounter(0);
        book1.setLanguage(language);
        book1.setPostdate(new Timestamp(981173106));
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
        book1.setUserid(user);
        GenreEng genre = new GenreEng();
        genre.setId(1);
        genre.setString("Fiction");
        genre.setCounter(0);
        book1.setGenreid(genre);
        book1.setCity(london);
        StatusEng statusActive = new StatusEng();
        statusActive.setId(1);
        statusActive.setString("Active");
        statusActive.setCounter(0);
        book1.setStatus(statusActive);
    }

    private static void createCities() {
        allCities = new ArrayList<>();
        newYork = new City();
        newYork.setId(1);
        newYork.setString("New York");
        newYork.setCounter(0);
        allCities.add(newYork);
        london = new City();
        london.setId(2);
        london.setString("London");
        london.setCounter(0);
        allCities.add(london);
        tallinn = new City();
        tallinn.setId(3);
        tallinn.setString("Tallinn");
        tallinn.setCounter(0);
        allCities.add(tallinn);
    }

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        createCities();
        createOneBook();
        booksServiceMock = mock(BooksService.class);
        cityServiceMock = mock(CityService.class);
        when(cityServiceMock.getAllCities()).thenReturn(allCities);
    }

    @Before
    public void setUpTest() {
        // before each test
        classifierController = new ClassifierController(booksServiceMock, cityServiceMock,
                null, null, null, null);
        allBooks = new ArrayList<>();
        reset(booksServiceMock);
        when(booksServiceMock.getAllBooks()).thenReturn(allBooks);
    }

    @Test
    public void testGetAllCitiesClassifierSorted() {
        allBooks.add(book1);
        List<City> cities = classifierController.getAllCities();
        assertEquals("London", cities.get(0).getString());
        assertEquals(1, cities.get(0).getCounter());
        assertEquals("New York", cities.get(1).getString());
        assertEquals(0, cities.get(1).getCounter());
        assertEquals("Tallinn", cities.get(2).getString());
        assertEquals(0, cities.get(2).getCounter());
    }
}
