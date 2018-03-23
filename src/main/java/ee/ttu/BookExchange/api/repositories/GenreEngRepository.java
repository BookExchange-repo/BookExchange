package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.GenreEng;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreEngRepository extends CrudRepository<GenreEng, Integer> {
    @Override
    List<GenreEng> findAll();
}
