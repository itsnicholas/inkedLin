package projekti.Skill;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.Account.Account;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    //@EntityGraph(attributePaths = {"likes"})
    //List<Skill> findAllByAccount(Account account, Pageable pageable);
}