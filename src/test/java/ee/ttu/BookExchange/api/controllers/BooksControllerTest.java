package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.BooksService;
import ee.ttu.BookExchange.api.services.StatusService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BooksControllerTest {
    @Mock
    private static BooksService booksServiceMock;

    @Mock
    private static StatusService statusServiceMock;

    private static List<Books> allBooks;
    private static BooksController booksController;

    private static void createSomeBooks() {
        Books book1 = new Books();
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
        City city = new City();
        city.setId(1);
        city.setString("New York");
        city.setCounter(0);
        user.setCity(city);
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
        book1.setCity(city);
        StatusEng status = new StatusEng();
        status.setId(1);
        status.setString("Active");
        status.setCounter(0);
        book1.setStatus(status);

        allBooks = new ArrayList<>();
        allBooks.add(book1);
    }

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        createSomeBooks();
        booksServiceMock = mock(BooksService.class);
        statusServiceMock = mock(StatusService.class);
        when(booksServiceMock.getAllBooks()).thenReturn(allBooks);
    }

    @Before
    public void setUpTest() {
        // before each test
        booksController = new BooksController(booksServiceMock, statusServiceMock);
    }

    @Test
    public void testGetBookInfo() {
        System.out.println(booksController.getBook(1, Optional.of("SESSION")));
    }
}
