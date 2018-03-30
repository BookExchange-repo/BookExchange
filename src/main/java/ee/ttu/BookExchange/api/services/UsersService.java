package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.Users;
import ee.ttu.BookExchange.api.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Users getUserByEmail(String userEmail) {
        Optional<Users> user = usersRepository.findAll().stream()
                .filter(e -> e.getEmail().equals(userEmail))
                .findFirst();
        return (user.isPresent()) ? user.get() : null;
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }
}
