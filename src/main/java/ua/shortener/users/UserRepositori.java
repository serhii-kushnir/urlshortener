package ua.shortener.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositori extends JpaRepository<User, Long> {
}