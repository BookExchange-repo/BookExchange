package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.GenreEst;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreEstRepository extends CrudRepository<GenreEst, Integer> {
    @Override
    List<GenreEst> findAll();
}
