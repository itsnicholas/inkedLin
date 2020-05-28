/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;
import projekti.Skill.Skill;

@Controller
public class PostController {
 
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    AccountService accountService;
    
    @GetMapping("/post")
    public String profile(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        
        return "post";
    }
    
    @PostMapping("/post")
    public String addPost(@RequestParam String content) {
        Account a = accountService.getUser();
        Post post = new Post();
        post.setAccount(a);
        post.setPostText(content);
        //post.setTimeCreated(LocalDateTime.MAX);
        postRepository.save(post);
        
        return "redirect:/post/";
    }
    
}
