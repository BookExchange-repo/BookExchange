package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Watchlist;
import ee.ttu.BookExchange.api.repositories.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class WatchlistService {
    private WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public void saveBook(int userId, int bookId) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUserid(userId);
        watchlist.setBookid(bookId);
        watchlistRepository.save(watchlist);
    }
}
