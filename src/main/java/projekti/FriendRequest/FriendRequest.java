package projekti.FriendRequest;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
    
    //private LocalDateTime timeCreated = LocalDateTime.now();
    
    @OneToOne
    private Account requester;
    
    @OneToOne
    private Account accepter;
    
}
