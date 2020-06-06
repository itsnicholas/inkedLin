package projekti.Account;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByPath(String path);
    List<Account> findByName(String name);
}
