package projekti;

import static java.time.LocalDateTime.now;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Comment.Comment;
import projekti.Comment.CommentRepository;
import projekti.FriendRequest.FriendRequest;
import projekti.FriendRequest.FriendRequestRepository;
import projekti.Post.Post;
import projekti.Post.PostRepository;
import projekti.Skill.Skill;
import projekti.Skill.SkillRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjektiTest {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Test
    public void addUserDatabaseSize() {
        int amountOfUsers = accountRepository.findAll().size();
        Account account = new Account("test", "testusername", "testpassword", "testpath", null, null, null, null, null);
        accountRepository.save(account);
        assertTrue(accountRepository.findAll().size() == amountOfUsers + 1);
    }
    
    @Test
    public void addUserDatabaseGetDetails() {
        Account account = new Account("test1", "testusername1", "testpassword1", "testpath1", null, null, null, null, null);
        accountRepository.save(account);
        assertEquals("test1", accountRepository.findByUsername("testusername1").getName());
        assertEquals("testpath1", accountRepository.findByUsername("testusername1").getPath());
    }
    
    @Test
    public void addUserDatabaseSameId() {
        Account account = new Account("test2", "testusername2", "testpassword2", "testpath2", null, null, null, null, null);
        accountRepository.save(account);
        Long accountId =  account.getId();
        Long accountId2 = accountRepository.findByUsername("testusername2").getId();
        assertTrue(accountId.equals(accountId2));
    }
    
    @Test
    public void addCommentDatabaseSizeAndId() {
        int amountOfComments = commentRepository.findAll().size();
        Comment comment = new Comment(null, now(), "testcomment");
        commentRepository.save(comment);
        assertTrue(commentRepository.findAll().size() == amountOfComments + 1);
        Long commentId =  comment.getId();
        Long commentId2 = commentRepository.getOne(commentId).getId();
        assertTrue(commentId.equals(commentId2));
    }
    
    @Test
    public void addFriendRequestDatabaseSizeAndId() {
        int amountOfFriendRequests = friendRequestRepository.findAll().size();
        FriendRequest friendRequest = new FriendRequest(null, null);
        friendRequestRepository.save(friendRequest);
        assertTrue(friendRequestRepository.findAll().size() == amountOfFriendRequests + 1);
        Long friendRequestId =  friendRequest.getId();
        Long friendRequestId2 = friendRequestRepository.getOne(friendRequestId).getId();
        assertTrue(friendRequestId.equals(friendRequestId2));
    }
    
    @Test
    public void addPostDatabaseSizeAndId() {
        int amountOfPosts = postRepository.findAll().size();
        Post post = new Post(null, now(), "testpost", null, null);
        postRepository.save(post);
        assertTrue(postRepository.findAll().size() == amountOfPosts + 1);
        Long postId =  post.getId();
        Long postId2 = postRepository.getOne(postId).getId();
        assertTrue(postId.equals(postId2));
    }
    
    @Test
    public void addSkillDatabaseSizeAndId() {
        int amountOfSkills = skillRepository.findAll().size();
        Skill skill = new Skill("skillname", null);
        skillRepository.save(skill);
        assertTrue(skillRepository.findAll().size() == amountOfSkills + 1);
        Long skillId =  skill.getId();
        Long skillId2 = skillRepository.getOne(skillId).getId();
        assertTrue(skillId.equals(skillId2));
    }
    
    @Test
    public void addTwoAccountsAsFriendsAndCheckIsItTrue() {
        Account account = new Account("test3", "testusername3", "testpassword3", "testpath3", null, null, null, null, new ArrayList<Account>());
        Account account2 = new Account("test4", "testusername4", "testpassword4", "testpath4", null, null, null, null, new ArrayList<Account>());
        account.getFriends().add(account2);
        account2.getFriends().add(account);
        assertTrue(account.getFriends().contains(account2));
        assertTrue(account2.getFriends().contains(account));
    }
    
    @Test
    public void addAll() {
        Account account = new Account("test3", "testusername3", "testpassword3", "testpath3", null, null, null, null, new ArrayList<Account>());
        Account account2 = new Account("test4", "testusername4", "testpassword4", "testpath4", null, null, null, null, new ArrayList<Account>());
        accountRepository.save(account);
        accountRepository.save(account2);
        
        Comment comment = new Comment(account, now(), "testcomment2");
        commentRepository.save(comment);
        
        FriendRequest friendRequest = new FriendRequest(account, account2);
        friendRequestRepository.save(friendRequest);
        
        Post post = new Post(account, now(), "testpost2", null, null);
        postRepository.save(post);
        
        Skill skill = new Skill("skillname2", null);
        skillRepository.save(skill);
        
        Long accountId = account.getId();
        Long accountId2 = account2.getId();
        Long commentId = comment.getId();
        Long friendRequestId = friendRequest.getId();
        Long postId = post.getId();
        Long skillId = skill.getId();
        
        assertFalse(accountRepository.getOne(accountId).getId() == null);
        assertFalse(accountRepository.getOne(accountId2).getId() == null);
        assertFalse(commentRepository.getOne(commentId).getId() == null);
        assertFalse(friendRequestRepository.getOne(friendRequestId).getId() == null);
        assertFalse(postRepository.getOne(postId).getId() == null);
        assertFalse(skillRepository.getOne(skillId).getId() == null);
    }
    
}
