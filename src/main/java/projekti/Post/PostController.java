/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.Post;

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
        Pageable pageable = PageRequest.of(0, 25, Sort.by("timeCreated").descending());
        model.addAttribute("posts", postRepository.findAll(pageable));

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
