package projekti.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import projekti.FriendRequest.FriendRequest;
import projekti.FriendRequest.FriendRequestRepository;
import projekti.Picture.Picture;
import projekti.Picture.PictureRepository;
import projekti.Skill.Skill;
import projekti.Skill.SkillRepository;

@Controller
public class AccountController {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    SkillRepository skillRepository;
    
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/signup")
    public String signup(@ModelAttribute Account account) {
        return "signup";
    }
    
    @RequestMapping("/logout")
    public String showLoggedOut(){
        return "login";
    }
 
    @RequestMapping("/login-error")  
    public String loginError(Model model) {  
        model.addAttribute("loginError", true);  
        return "login";  
    }

    @GetMapping("/profile")
    public String authentication() {
        Account current = accountService.getUser();
        return "redirect:/profile/" + current.getPath();
    }
    
    @GetMapping("/profile/{path}")
    public String profile(Model model, @PathVariable String path) {
        model.addAttribute("viewedAccount", accountRepository.findByPath(path));
        model.addAttribute("account", accountService.getUser());
        model.addAttribute("path", path);
        model.addAttribute("picture", accountRepository.findByPath(path).getPicture());
        model.addAttribute("profileName", accountRepository.findByPath(path).getName());
        model.addAttribute("friends", accountRepository.findByPath(path).getFriends());
        model.addAttribute("friendRequests", accountRepository.findByPath(path).getReceivedFriendRequest());
        model.addAttribute("sentRequests", accountRepository.findByPath(path).getSentFriendRequest());
        
        List<Skill> skills = accountRepository.findByPath(path).getSkills();
        Collections.sort(skills);
        List<Skill> first3ElementsList = skills.stream().limit(3).collect(Collectors.toList());
        List<Skill> nextElementsList = new ArrayList<>();

        for (int i = 3; i < skills.size(); i++) {
            if (skills.get(i) != null) {
                nextElementsList.add(skills.get(i));
            }
        }

        model.addAttribute("skills", first3ElementsList);
        model.addAttribute("skills2", nextElementsList);
        
        return "profile";
    }
    
    @GetMapping("/userlist")
    public String Users(Model model) {
        model.addAttribute("user", accountService.getUser());
        Account a = accountService.getUser();
        model.addAttribute("user", a);
        List<Account> allexceptUser = accountRepository.findAllByOrderByNameAsc();
        allexceptUser.remove(a);
        model.addAttribute("accounts", allexceptUser);
        
        return "userlist";
    }
    
    @PostMapping("/signup")
    public String add(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "signup";
        }
        try {
            if (accountRepository.findByUsername(account.getUsername()) != null) {
                bindingResult.rejectValue("username", "username.error", " This username is in use already!");
                return "signup"; 
            }
            if (accountRepository.findByPath(account.getPath()) != null) {
                bindingResult.rejectValue("path", "path.error", " This path is in use already!");
                return "signup";
            }
            
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            return "redirect:/profile";
        } catch (DataIntegrityViolationException ex) {
            bindingResult.rejectValue("username", "username.error", "Please try again!");
            return "signup";
        }
    }
    
    @PostMapping("/profile/{path}/picture")
    public String addPicture(@PathVariable String path, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.getContentType().equals("image/jpeg")) {
            Account a = accountRepository.findByPath(path);
            Picture pic = new Picture();
            pic.setContent(file.getBytes());
            pictureRepository.save(pic);
            a.setPicture(pic);
            accountRepository.save(a);
        } 
        return "redirect:/profile/" + path;
    }
    
    @PostMapping("/profile/{path}/picture/delete")
    public String deletePicture(@PathVariable String path) {
            Account a = accountRepository.findByPath(path);
            a.setPicture(null);
            accountRepository.save(a);
        return "redirect:/profile/" + path;
    }
    
    @GetMapping(path = "picture/{id}", produces = "image/jpeg")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id) {
        return pictureRepository.getOne(id).getContent();
    }
    
    @PostMapping("/friend/{id}/friendRequest")
    public String friendRequest(@PathVariable Long id) {
        Account requester = accountService.getUser();
        Account accepter = accountRepository.getOne(id);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setAccepter(accepter);
        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setRequester(accepter);
        friendRequest2.setAccepter(requester);
        friendRequestRepository.save(friendRequest);
        friendRequestRepository.save(friendRequest2);
        
        if (!requester.getSentFriendRequest().contains(friendRequest)) {
            if (!requester.getReceivedFriendRequest().contains(friendRequest2)) {
                requester.getSentFriendRequest().add(friendRequest);
                accepter.getReceivedFriendRequest().add(friendRequest);
                accountRepository.save(requester);
                accountRepository.save(accepter);
            }
        }

        return "redirect:/userlist";
    }
    
    @PostMapping("/profile/{path}/friend/{id}/add")
    public String friendAdd(@PathVariable String path, @PathVariable Long id) {
        Account accepter = accountRepository.findByPath(path);
        Account requester = friendRequestRepository.getOne(id).getRequester();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setAccepter(accepter);

        if (requester.getSentFriendRequest().contains(friendRequest)) {
                requester.getSentFriendRequest().remove(friendRequest);
                accountRepository.save(requester);
        }
        
        if (accepter.getReceivedFriendRequest().contains(friendRequest)) {
                accepter.getReceivedFriendRequest().remove(friendRequest);
                accountRepository.save(accepter);
        }
        
        if (accepter.getFriends().contains(requester)) {  
            return "redirect:/profile/" + path;
        }
        
        accepter.getFriends().add(requester);
        requester.getFriends().add(accepter);
        accountRepository.save(accepter);
        accountRepository.save(requester);
        
        return "redirect:/profile/" + path;
    }
    
    @PostMapping("/profile/{path}/friend/{id}/notAdd")
    public String friendNotAdd(@PathVariable String path, @PathVariable Long id) {
        Account accepter = accountRepository.findByPath(path);
        Account requester = friendRequestRepository.getOne(id).getRequester();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setAccepter(accepter);
        
        if (requester.getSentFriendRequest().contains(friendRequest)) {
            if (accepter.getReceivedFriendRequest().contains(friendRequest)) {
                requester.getSentFriendRequest().remove(friendRequest);
                accepter.getReceivedFriendRequest().remove(friendRequest);
                accountRepository.save(requester);
                accountRepository.save(accepter);
            }
        }
        
        return "redirect:/profile/" + path;
    }
    
    @PostMapping("/profile/{path}/friend/{id}/delete")
    public String friendDelete(@PathVariable String path, @PathVariable Long id) {
        Account current = accountRepository.findByPath(path);
        
        List<Account> friendList = current.getFriends();
                
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).getId() == id) {
                Account remove = friendList.get(i);
                friendList.remove(remove);
                remove.getFriends().remove(current);
                accountRepository.save(remove);
            }
        }
        
        current.setFriends(friendList);       
        accountRepository.save(current);
        
        return "redirect:/profile/" + path;
    }
    
    @PostMapping("/search")
    public String accountSearch(Model model, @RequestParam String name) {
        Account user =  accountService.getUser();
        List<Account> allAccounts = accountRepository.findAll();
        List<Account> suitableAccounts = new ArrayList<>();
        for (int i = 0; i < allAccounts.size(); i++) {
            if (allAccounts.get(i).getName().toLowerCase().contains(name.toLowerCase()) 
                    && allAccounts.get(i) != user) {
                suitableAccounts.add(allAccounts.get(i));
            }
        }
        model.addAttribute("user", accountService.getUser());
        model.addAttribute("accounts", suitableAccounts);

        return "userlist";
    }
    
    @PostMapping("/all")
    public String accountAll() {
            return "redirect:/userlist";
    }
    
}
