package projekti.FriendRequest;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Account.Account;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest extends AbstractPersistable<Long> {
    
    @OneToOne
    private Account requester;
    
    @OneToOne
    private Account accepter;
    
}
