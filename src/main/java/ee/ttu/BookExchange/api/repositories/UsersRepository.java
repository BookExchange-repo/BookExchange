package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<Users, Integer> {
    @Override
    List<Users> findAll();
}
