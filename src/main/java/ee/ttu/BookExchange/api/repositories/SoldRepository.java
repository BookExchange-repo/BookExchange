package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.Sold;
import ee.ttu.BookExchange.api.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoldRepository extends CrudRepository<Sold, Integer> {
    @Override
    List<Sold> findAll();

    List<Sold> findByUseridIn(Users userid);
}
