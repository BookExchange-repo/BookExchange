package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.ConditionEng;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionEngRepository extends CrudRepository<ConditionEng, Integer> {
    @Override
    List<ConditionEng> findAll();
}
