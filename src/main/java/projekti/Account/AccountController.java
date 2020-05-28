package projekti.Account;

import projekti.Account.Account;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
        
        //model.addAttribute("skills", accountRepository.findByPath(path).getSkills());
        //model.addAttribute("skills2", accountRepository.findByPath(path).getSkills());
        
        return "profile";
    }
    
    @GetMapping("/userlist")
    public String Users(Model model) {
        model.addAttribute("accounts", accountRepository.findAll()); // kaikki tilit
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
            //Picture pic = accountRepository.findByPath(path).getPicture(); Tarvitaanko tätä toimivassa muodossa?
            //pictureRepository.delete(pic); Tarvitaanko tätä toimivassa muodossa?
            a.setPicture(null);
            accountRepository.save(a);
        return "redirect:/profile/" + path;
    }
    
    @GetMapping(path = "picture/{id}", produces = "image/jpeg")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id) {
        return pictureRepository.getOne(id).getContent();
    }
}
