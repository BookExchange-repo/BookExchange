package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.LanguageEst;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageEstRepository extends CrudRepository<LanguageEst, Integer> {
    @Override
    List<LanguageEst> findAll();
}
