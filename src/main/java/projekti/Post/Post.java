package projekti.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class Post extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account account;
    
    @NotNull
    private LocalDateTime timeCreated = LocalDateTime.now();
    
    @NotEmpty
    @Size(min = 1, max = 500)
    private String postText;
    
    @ManyToMany
    @JoinTable(
        name="who_liked_post",
        joinColumns=
            @JoinColumn(name="post_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="like_account_id", referencedColumnName="id"))
    private List<Account> likes = new ArrayList<>();
    
}