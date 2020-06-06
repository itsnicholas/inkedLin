package projekti.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;
import projekti.Comment.Comment;
import projekti.Comment.CommentRepository;

@Controller
public class PostController {
 
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    AccountService accountService;
    
    @GetMapping("/post")
    public String profile(Model model) {
        Account a = accountService.getUser();
        List<Account> aFriendsAndA = a.getFriends();
        aFriendsAndA.add(a);
        List<Post> allPosts = postRepository.findAll();
        List<Post> allOfRiendsPosts = new ArrayList<>();
        
        for (int i = 0; i < allPosts.size(); i++) {
            for (int j = 0; j < aFriendsAndA.size(); j++) {
                if (allPosts.get(i).getAccount() == aFriendsAndA.get(j)) {
                    allOfRiendsPosts.add(allPosts.get(i));
                }
            }
        }
        
        Collections.sort(allOfRiendsPosts);
        List<Post> first25ElementsList = allOfRiendsPosts.stream().limit(25).collect(Collectors.toList());
        
        //Pageable pageable = PageRequest.of(0, 25, Sort.by("timeCreated").descending());
        model.addAttribute("posts", first25ElementsList);
        model.addAttribute("user", accountService.getUser());

        return "post";
    }
    
    @PostMapping("/post")
    public String addPost(@RequestParam String content) {
        Account a = accountService.getUser();
        Post post = new Post();
        post.setAccount(a);
        post.setPostText(content);
        postRepository.save(post);
        
        return "redirect:/post/";
    }
    
    @PostMapping("/post/{id}/like")
    public String postLike(@PathVariable Long id) {
        Account liker = accountService.getUser();
        Post post = postRepository.getOne(id);
            
        if (post.getLikes().contains(liker)) {
            post.getLikes().remove(liker);
            postRepository.save(post);
            
            return "redirect:/post/";
        }
            
        post.getLikes().add(liker);
        postRepository.save(post);
            
        return "redirect:/post/";
    }
    
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content) {
        Account a = accountService.getUser();
        Comment comment = new Comment();
        comment.setAccount(a);
        comment.setCommentText(content);
        commentRepository.save(comment);
        Post post = postRepository.getOne(id);
        postRepository.getOne(id).getMessageComments().add(comment);
        postRepository.save(post);
        
        return "redirect:/post/";
    }
}
