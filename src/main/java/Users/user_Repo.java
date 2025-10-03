package Users;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface user_Repo extends JpaRepository<user,Long> {
    Optional<user>findByUsername(String Username);
    Optional<user>findByEmail(String Email);
    boolean exitsByUsername(String Username);
    boolean existsByUserEmail(String Email);
}
