package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.Books;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends CrudRepository<Books, Integer> {
    @Override
    List<Books> findAll();
}
