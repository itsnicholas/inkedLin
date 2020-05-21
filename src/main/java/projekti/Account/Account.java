package projekti.Account;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Picture.Picture;

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
    private String username;
    
    @NotEmpty
    @Size(min = 4)
    private String password;
    
    @NotEmpty
    @Size(min = 4, max = 16)
    private String path;
    
    @OneToOne
    private Picture picture;
    
}
