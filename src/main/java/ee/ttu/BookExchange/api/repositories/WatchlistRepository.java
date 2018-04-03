package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.Watchlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends CrudRepository<Watchlist, Integer> {
    @Override
    List<Watchlist> findAll();
}
