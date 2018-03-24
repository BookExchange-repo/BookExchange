package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.repositories.BooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {
    private BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Books getBookById(int bookId) {
        return booksRepository.findOne(bookId);
    }

    public void saveBook(Books book) {
        booksRepository.save(book);
    }
}
