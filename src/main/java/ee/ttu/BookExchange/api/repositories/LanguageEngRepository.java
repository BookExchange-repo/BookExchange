package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.LanguageEng;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageEngRepository extends CrudRepository<LanguageEng, Integer> {
    @Override
    List<LanguageEng> findAll();
}
