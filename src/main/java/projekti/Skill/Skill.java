
package projekti.Skill;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Account.Account;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> implements Comparable<Skill> {
    
    @NotEmpty
    @Size(min = 1, max = 50)
    @Column
    private String name;
    
    @ManyToMany
    @JoinTable(
        name="who_liked",
        joinColumns=
            @JoinColumn(name="skill_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="like_account_id", referencedColumnName="id"))
    private List<Account> likes;

    @Override
    public int compareTo(Skill skill) {
        if (this.getLikes().size() < skill.getLikes().size()) {
            return 1;
        } else if (this.getLikes().size() > skill.getLikes().size()) {
            return -1;
        } else {
            return 0;
        }
    }
}