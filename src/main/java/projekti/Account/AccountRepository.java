package projekti.Account;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.Skill.Skill;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByPath(String path);
    //Account findBySkills (List<Skill> skills, Pageable pageable);
}
