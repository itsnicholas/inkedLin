package projekti.Account;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.FriendRequest.FriendRequest;
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
    
    @Pattern(regexp = "[\\w]*")
    @NotEmpty
    @Size(min = 4, max = 16)
    @Column(unique = true)
    private String path;
    
    @OneToOne
    private Picture picture;
    
    @OneToMany
    private List<Skill> skills;
    
    @OneToMany
    private List<FriendRequest> sentFriendRequest;
    
    @OneToMany
    private List<FriendRequest> receivedFriendRequest;
    
    @ManyToMany
    @JoinTable(
        name="friends",
        joinColumns=
            @JoinColumn(name="account_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="friend_id", referencedColumnName="id")
    )
    
    private List<Account> friends;
 
}
