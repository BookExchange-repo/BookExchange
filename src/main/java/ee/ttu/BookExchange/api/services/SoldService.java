package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Sold;
import ee.ttu.BookExchange.api.repositories.SoldRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoldService {
    private SoldRepository soldRepository;
    private BooksService booksService;
    private UsersService usersService;

    public SoldService(SoldRepository soldRepository,
                       BooksService booksService,
                       UsersService usersService)
    {
        this.soldRepository = soldRepository;
        this.booksService = booksService;
        this.usersService = usersService;
    }

    public void saveBook(int userId, int bookId) {
        Sold soldlist = new Sold();
        soldlist.setUserid(usersService.getUserById(userId));
        soldlist.setBookid(booksService.getBookById(bookId));
        soldRepository.save(soldlist);
    }

    public List<Sold> findByUserId(int userId) {
        return soldRepository.findByUseridIn(usersService.getUserById(userId));
    }
}
