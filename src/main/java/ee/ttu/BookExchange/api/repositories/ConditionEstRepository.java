package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.ConditionEst;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionEstRepository extends CrudRepository<ConditionEst, Integer> {
    @Override
    List<ConditionEst> findAll();
}
