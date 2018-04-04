package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Watchlist;
import ee.ttu.BookExchange.api.repositories.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {
    private WatchlistRepository watchlistRepository;
    private BooksService booksService;
    private UsersService usersService;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            BooksService booksService,
                            UsersService usersService)
    {
        this.watchlistRepository = watchlistRepository;
        this.booksService = booksService;
        this.usersService = usersService;
    }

    public void saveBook(int userId, int bookId) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUserid(usersService.getUserById(userId));
        watchlist.setBookid(booksService.getBookById(bookId));
        watchlistRepository.save(watchlist);
    }

    public List<Watchlist> findByUserId(int userId) {
        return watchlistRepository.findByUseridIn(usersService.getUserById(userId));
    }
}
