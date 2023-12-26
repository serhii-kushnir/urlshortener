package ua.shortener.user.servise;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shortener.user.User;

public interface UserRepository extends JpaRepository<User, String> {
}
