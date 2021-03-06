package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.BooksService;
import ee.ttu.BookExchange.api.services.StatusService;
import ee.ttu.BookExchange.api.services.UsersService;
import ee.ttu.BookExchange.api.services.WatchlistService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

@RunWith(PowerMockRunner.class)
public class BooksControllerTest {
    @Mock
    private static BooksService booksServiceMock;

    @Mock
    private static UsersService usersServiceMock;

    @Mock
    private static StatusService statusServiceMock;

    @Mock
    private static WatchlistService watchlistServiceMock;

    private static List<Books> allBooks;
    private static BooksController booksController;
    private static Books book1;
    private static StatusEst statusActive;
    private static StatusEst statusSold;
    private static StatusEst statusCancelled;
    private static City newYork;
    private static City london;
    private static City tallinn;
    private static Users user;

    private static void createOneBook() {
        book1 = new Books();
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
        book1.setGenreid(genre);
        book1.setCity(city);
        book1.setStatus(statusActive);
    }

    public static void createStatuses() {
        statusActive = new StatusEst();
        statusActive.setId(1);
        statusActive.setString("Aktiivne");
        statusActive.setCounter(0);
        statusSold = new StatusEst();
        statusSold.setId(2);
        statusSold.setString("Müüdud");
        statusSold.setCounter(0);
        statusCancelled = new StatusEst();
        statusCancelled.setId(3);
        statusCancelled.setString("Tühistatud");
        statusCancelled.setCounter(0);
    }

    private static void createCities() {
        newYork = new City();
        newYork.setId(1);
        newYork.setString("New York");
        newYork.setCounter(0);
        london = new City();
        london.setId(2);
        london.setString("London");
        london.setCounter(0);
        tallinn = new City();
        tallinn.setId(3);
        tallinn.setString("Tallinn");
        tallinn.setCounter(0);
    }

    private static void createUser() {
        user = new Users();
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
    }

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        createStatuses();
        createCities();
        createUser();
        createOneBook();
        booksServiceMock = mock(BooksService.class);
        usersServiceMock = mock(UsersService.class);
        statusServiceMock = mock(StatusService.class);
        watchlistServiceMock = mock(WatchlistService.class);
        stub(method(UsersController.class, "getUserIdBySession")).toReturn(Optional.of(123));
    }

    @Before
    public void setUpTest() {
        // before each test
        booksController = new BooksController(booksServiceMock, usersServiceMock,
                statusServiceMock, watchlistServiceMock);
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
    @PrepareForTest(UsersController.class)
    public void testAddOneNewBook() {
        makeReadyBookToListAndStub(book1);
        Map<String, Object> mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertTrue(mapOfBooks.containsKey("errors"));
        assertTrue(((List<Books>)(mapOfBooks.get("errors"))).isEmpty());
        assertTrue(mapOfBooks.containsKey("books"));
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(1, listOfBooks.size());
        Books newBook = new Books();
        newBook.setTitle("NEW Book_Title123");
        newBook.setDescription("Description OF NEW book");
        booksController.addBook(Optional.of("SESSION"), newBook);
        verify(booksServiceMock).saveBook(newBook);
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooks() {
        makeReadyBookToListAndStub(book1);
        Map<String, Object> mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertTrue(mapOfBooks.containsKey("errors"));
        assertTrue(((List<Books>)(mapOfBooks.get("errors"))).isEmpty());
        assertTrue(mapOfBooks.containsKey("books"));
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(1, listOfBooks.size());
        Books newBook = new Books();
        Users oneTwoThree = new Users();
        oneTwoThree.setId(123);
        newBook.setUserid(oneTwoThree);
        newBook.setStatus(statusActive);
        newBook.setTitle("NEW Book_Title123");
        newBook.setDescription("Description OF NEW book");
        allBooks.add(newBook);
        mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(2, listOfBooks.size());
        assertEquals("Good book", listOfBooks.get(0).getTitle());
        assertEquals("NEW Book_Title123", listOfBooks.get(1).getTitle());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetBookInfo() {
        makeReadyBookToListAndStub(book1);
        Books gottenBook = booksController.getBook(1, Optional.empty());
        assertEquals("Good book", gottenBook.getTitle());
        assertEquals("This is a description of a book.", gottenBook.getDescription());
        assertEquals(981173106, gottenBook.getPostdate().getTime());
        assertEquals("", gottenBook.getUserid().getEmail()); // hidden
        assertEquals("Some One", gottenBook.getUserid().getFull_name());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetBookInfoSession() {
        makeReadyBookToListAndStub(book1);
        Books gottenBook = booksController.getBook(1, Optional.of("SESSION"));
        assertEquals("Good book", gottenBook.getTitle());
        assertEquals("This is a description of a book.", gottenBook.getDescription());
        assertEquals(981173106, gottenBook.getPostdate().getTime());
        //assertEquals("some@user.com", gottenBook.getUserid().getEmail());
        assertEquals("", gottenBook.getUserid().getEmail());
        assertEquals("Some One", gottenBook.getUserid().getFull_name());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooksSortTitle() {
        List<Users> temporaryUserList = new ArrayList<>();
        temporaryUserList = UsersControllerTest.createUser(temporaryUserList);
        Users newUser = temporaryUserList.get(0);

        Books bookBbb = new Books();
        bookBbb.setUserid(newUser);
        bookBbb.setStatus(statusActive);
        bookBbb.setTitle("BBB");
        bookBbb.setDescription("BBBdesc");
        Books bookAaa = new Books();
        bookAaa.setUserid(newUser);
        bookAaa.setStatus(statusActive);
        bookAaa.setTitle("AAA");
        bookAaa.setDescription("AAAdesc");
        Books bookCcc = new Books();
        bookCcc.setUserid(newUser);
        bookCcc.setStatus(statusActive);
        bookCcc.setTitle("CCC");
        bookCcc.setDescription("CCCdesc");
        makeReadyBookToListAndStub(bookBbb, bookAaa, bookCcc);
        Map<String, Object> mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.of("title"), Optional.empty(), Optional.of("SESSION"), Optional.empty(), Optional.empty());
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals("AAA", listOfBooks.get(0).getTitle());
        assertEquals("BBB", listOfBooks.get(1).getTitle());
        assertEquals("CCC", listOfBooks.get(2).getTitle());
        mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.of("title"), Optional.of(true), Optional.of("SESSION"), Optional.empty(), Optional.empty());
        listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals("CCC", listOfBooks.get(0).getTitle());
        assertEquals("BBB", listOfBooks.get(1).getTitle());
        assertEquals("AAA", listOfBooks.get(2).getTitle());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooksSortPrice() {
        List<Users> temporaryUserList = new ArrayList<>();
        temporaryUserList = UsersControllerTest.createUser(temporaryUserList);
        Users newUser = temporaryUserList.get(0);

        Books bookBbb = new Books();
        bookBbb.setUserid(newUser);
        bookBbb.setStatus(statusActive);
        bookBbb.setPrice(new BigDecimal("20.10"));
        bookBbb.setDescription("BBBdesc");
        Books bookAaa = new Books();
        bookAaa.setUserid(newUser);
        bookAaa.setStatus(statusActive);
        bookAaa.setPrice(new BigDecimal("0.50"));
        bookAaa.setDescription("AAAdesc");
        Books bookCcc = new Books();
        bookCcc.setUserid(newUser);
        bookCcc.setStatus(statusActive);
        bookCcc.setPrice(new BigDecimal("999"));
        bookCcc.setDescription("CCCdesc");
        makeReadyBookToListAndStub(bookBbb, bookAaa, bookCcc);
        Map<String, Object> mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.of("price"), Optional.empty(), Optional.of("SESSION"), Optional.empty(), Optional.empty());
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals("0.50", listOfBooks.get(0).getPrice().toString());
        assertEquals("20.10", listOfBooks.get(1).getPrice().toString());
        assertEquals("999", listOfBooks.get(2).getPrice().toString());
        mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.of("price"), Optional.of(true), Optional.of("SESSION"), Optional.empty(), Optional.empty());
        listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals("999", listOfBooks.get(0).getPrice().toString());
        assertEquals("20.10", listOfBooks.get(1).getPrice().toString());
        assertEquals("0.50", listOfBooks.get(2).getPrice().toString());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooksFilterCity() {
        Books book1 = new Books();
        book1.setUserid(user);
        book1.setStatus(statusActive);
        book1.setTitle("Good book");
        book1.setCity(newYork);
        Books book2 = new Books();
        book2.setUserid(user);
        book2.setStatus(statusActive);
        book2.setTitle("Bad book");
        book2.setCity(london);
        Books book3 = new Books();
        book3.setUserid(user);
        book3.setStatus(statusActive);
        book3.setTitle("SOMEBOOK");
        book3.setCity(tallinn);
        makeReadyBookToListAndStub(book1, book2, book3);

        Map<String, String> params = new HashMap<>();
        params.put("city", "3");
        Map<String, Object> mapOfBooks = booksController.getAllBooks(params,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(1, listOfBooks.size());
        assertEquals("SOMEBOOK", listOfBooks.get(0).getTitle());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooksFilterCitySession() {
        Books book1 = new Books();
        book1.setUserid(user);
        book1.setStatus(statusActive);
        book1.setTitle("Good book");
        book1.setCity(newYork);
        Books book2 = new Books();
        book2.setUserid(user);
        book2.setStatus(statusActive);
        book2.setTitle("Bad book");
        book2.setCity(london);
        Books book3 = new Books();
        book3.setUserid(user);
        book3.setStatus(statusActive);
        book3.setTitle("SOMEBOOK");
        book3.setCity(tallinn);
        makeReadyBookToListAndStub(book1, book2, book3);

        Map<String, String> params = new HashMap<>();
        params.put("city", "3");
        params.put("session", "SESSION");
        Map<String, Object> mapOfBooks = booksController.getAllBooks(params,
                Optional.empty(), Optional.empty(), Optional.of("SESSION"), Optional.empty(), Optional.empty());
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(1, listOfBooks.size());
        assertEquals("SOMEBOOK", listOfBooks.get(0).getTitle());
    }

    @Test
    @PrepareForTest(UsersController.class)
    public void testGetAllBooksShowOnlyActive() {
        Books book1 = new Books();
        book1.setUserid(user);
        book1.setStatus(statusActive);
        book1.setTitle("Good book");
        book1.setCity(newYork);
        Books book2 = new Books();
        book2.setUserid(user);
        book2.setStatus(statusSold);
        book2.setTitle("Bad book");
        book2.setCity(london);
        Books book3 = new Books();
        book3.setUserid(user);
        book3.setStatus(statusActive);
        book3.setTitle("SOMEBOOK");
        book3.setCity(tallinn);
        Books book4 = new Books();
        book4.setUserid(user);
        book4.setStatus(statusCancelled);
        book4.setTitle("B00k...");
        book4.setCity(newYork);
        makeReadyBookToListAndStub(book1, book2, book3, book4);

        Map<String, Object> mapOfBooks = booksController.getAllBooks(new HashMap<>(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<Books> listOfBooks = (List<Books>)mapOfBooks.get("books");
        assertEquals(2, listOfBooks.size());
        assertEquals("Good book", listOfBooks.get(0).getTitle());
        assertEquals("SOMEBOOK", listOfBooks.get(1).getTitle());
    }
}
