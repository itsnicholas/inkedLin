/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.Skill;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekti.Account.Account;
import projekti.Account.AccountRepository;

@Controller
public class SkillController {
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @PostMapping("/profile/{path}/skill")
    public String addSkill(@PathVariable String path, @RequestParam String content) {
            Account a = accountRepository.findByPath(path);
            Skill skill = new Skill();
            skill.setSkillName(content);
            skillRepository.save(skill);
            a.getSkills().add(skill);
            accountRepository.save(a);
            return "redirect:/profile/" + path;
    }
    
}