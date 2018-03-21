package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(int userId) {
        return usersRepository.findOne(userId);
    }
}
