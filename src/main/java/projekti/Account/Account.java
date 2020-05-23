package projekti.Account;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Picture.Picture;
import projekti.Skill.Skill;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min = 4, max = 16)
    private String name;
    
    @NotEmpty
    @Size(min = 4, max = 16)
    @Column(unique = true)
    private String username;
    
    @NotEmpty
    @Size(min = 4)
    private String password;
    
    @NotEmpty
    @Size(min = 4, max = 16)
    @Column(unique = true)
    private String path;
    
    @OneToOne
    private Picture picture;
    
    @OneToMany
    private List<Skill> skills;
    
}
