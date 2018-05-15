package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StatsControllerTest {
    @Mock
    private static BooksService booksServiceMock;

    @Mock
    private static WatchlistService watchlistServiceMock;

    private static StatsController statsController;
    private static List<Books> allBooks;

    private static Books createOneBook() {
        Books book1 = new Books();
        book1.setId(1);
        book1.setTitle("Good book");
        book1.setAuthor("Author");
        book1.setDescription("This is a description of a book.");
        ConditionEst condition = new ConditionEst();
        condition.setId(1);
        condition.setString("Hea seisukord");
        condition.setCounter(0);
        book1.setConditiondesc(condition);
        book1.setPrice(new BigDecimal("123.45"));
        book1.setLikes(0);
        book1.setIsbn("1234567890123");
        book1.setImagepath("http://example.com/image/path.jpg");
        book1.setPublisher("Publisher Inc.");
        book1.setPubyear("2001");
        LanguageEst language = new LanguageEst();
        language.setId(1);
        language.setString("Inglise");
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
        GenreEst genre = new GenreEst();
        genre.setId(1);
        genre.setString("Ilukirjandus");
        genre.setCounter(0);
        StatusEst statusActive = new StatusEst();
        statusActive.setId(1);
        statusActive.setString("Aktiivne");
        statusActive.setCounter(0);
        book1.setGenreid(genre);
        book1.setCity(city);
        book1.setStatus(statusActive);
        return book1;
    }

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        booksServiceMock = mock(BooksService.class);
        watchlistServiceMock = mock(WatchlistService.class);
        when(watchlistServiceMock.findAmountByBookId(any(Integer.class))).thenReturn((long)123);
    }

    @Before
    public void setUpTest() {
        // before each test
        statsController = new StatsController(booksServiceMock, watchlistServiceMock);
    }

    private void makeReadyBookToListAndStub(Books... bookList) {
        allBooks = new ArrayList<>();
        reset(booksServiceMock);
        when(booksServiceMock.getAllBooks()).thenReturn(allBooks);
        for (int i = 0; i < bookList.length; i++) {
            allBooks.add(bookList[i]);
            when(booksServiceMock.getBookById(i+1)).thenReturn(allBooks.get(i));
        }
    }

    @Test
    public void testMostPopularCategory() {
        Books book1 = createOneBook();
        book1.getGenreid().setId(1);
        book1.getGenreid().setString("CategoryA");
        Books book2 = createOneBook();
        book2.getGenreid().setId(2);
        book2.getGenreid().setString("CategoryB");
        Books book3 = createOneBook();
        book3.getGenreid().setId(2);
        book3.getGenreid().setString("CategoryB");
        Books book4 = createOneBook();
        book4.getGenreid().setId(2);
        book4.getGenreid().setString("CategoryB");
        Books book5 = createOneBook();
        book5.getGenreid().setId(3);
        book5.getGenreid().setString("CategoryC");
        Books book6 = createOneBook();
        book6.getGenreid().setId(3);
        book6.getGenreid().setString("CategoryC");
        makeReadyBookToListAndStub(book1, book2, book3, book4, book5, book6);
        assertEquals("CategoryB",
                ((GenreEst)(statsController.getStatistics().get("mostPopularGenre"))).getString());
    }

    @Test
    public void testAveragePrice() {
        Books book1 = createOneBook();
        book1.setPrice(new BigDecimal("37.08151804"));
        Books book2 = createOneBook();
        book2.setPrice(new BigDecimal("547.94081574"));
        Books book3 = createOneBook();
        book3.setPrice(new BigDecimal("3086.29335590"));
        Books book4 = createOneBook();
        book4.setPrice(new BigDecimal("2.5"));
        Books book5 = createOneBook();
        book5.setPrice(new BigDecimal("0"));
        Books book6 = createOneBook();
        book6.setPrice(new BigDecimal("1"));
        makeReadyBookToListAndStub(book1, book2, book3, book4, book5, book6);
        assertEquals("612.47", statsController.getStatistics().get("averageBookPrice").toString());
    }

    @Test
    public void testTotalBooks() {
        Books book = createOneBook();
        int bookAmount = new Random().nextInt(900) + 1;
        allBooks = new ArrayList<>();
        for (int i = 0; i < bookAmount; i++) {
            allBooks.add(book);
        }
        reset(booksServiceMock);
        when(booksServiceMock.getAllBooks()).thenReturn(allBooks);
        assertEquals(bookAmount, (int)(statsController.getStatistics().get("totalBooks")));
    }
}
