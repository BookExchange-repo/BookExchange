package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.StatusEng;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusEngRepository extends CrudRepository<StatusEng, Integer> {
    @Override
    List<StatusEng> findAll();
}
