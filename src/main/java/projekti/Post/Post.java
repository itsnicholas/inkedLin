package projekti.Post;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Account.Account;
import projekti.Comment.Comment;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> implements Comparable<Post>{
    
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
    private List<Account> likes;
    
    @OneToMany
    @OrderBy("timeCreated DESC")
    private List<Comment> messageComments;
    
    @Override
    public int compareTo(Post post) {
        if (this.getTimeCreated().isBefore(post.getTimeCreated())) {
            return 1;
        } else if (this.getTimeCreated().isAfter(post.getTimeCreated())) {
            return -1;
        } else {
            return 0;
        }
    }
    
}