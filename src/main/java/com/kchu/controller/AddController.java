package com.kchu.controller;
import com.kchu.models.*;
import com.kchu.repositorySQL.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

/**
 * Created by student on 6/30/17.
 */
@Controller
@SessionAttributes("loginId")
public class AddController {

    @Autowired
    private EduRepository eduRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private SkillRepository skillRepository;


    //Add education group
    @GetMapping(value="/addedu/{uid}")
    public String addEdu(@PathVariable("uid")Integer uid, Model model){
        model.addAttribute(new Education());
        return "addedu";
    }

    @PostMapping(value="/addedu/{uid}")
    public String addEduNext(@PathVariable("uid")Integer uid, @RequestParam(value="action", required=true) String action,
                             @ModelAttribute Education education, Model model) {

        model.addAttribute("uid", uid.intValue());
        model.addAttribute(new Education());
        education.setUserId(uid);
        String next = "";
        switch (action) {
            case ("add1"):
                eduRepository.save(education);
                next = "addedu";
                break;
            case ("submit"):
                eduRepository.save(education);
                next = "redirect:/addwork/" + uid;
                break;
            case ("skip"):
                next = "redirect:/addwork/" + uid;
                break;
        }

        return next;
    }

    //Add work group
    @GetMapping(value="/addwork/{uid}")
    public String addwork(@PathVariable("uid")Integer uid, Model model){
        model.addAttribute(new Work());
        return "addwork";
    }

    @PostMapping(value="/addwork/{uid}")
    public String addWorkNext(@PathVariable("uid")Integer uid, @RequestParam(value="action", required=true) String action,
                             @ModelAttribute Work work, Model model) {

        model.addAttribute("uid", uid.intValue());
        model.addAttribute(new Work());
        work.setUserId(uid);
        String next = "";
        switch (action) {
            case ("add1"):
                workRepository.save(work);
                next = "addwork";
                break;
            case ("submit"):
                workRepository.save(work);
                next = "redirect:/addskill/" + uid;
                break;
            case ("skip"):
                next = "redirect:/addskill/" + uid;
                break;
        }
        return next;
    }

    //Add skills group
    @GetMapping(value="/addskill/{uid}")
    public String addskill(@PathVariable("uid")Integer uid, Model model){
        model.addAttribute(new Skill());
        return "addskill";
    }

    @PostMapping(value="/addskill/{uid}")
    public String addSkillNext(@PathVariable("uid")Integer uid, @RequestParam(value="action", required=true) String action,
                              @ModelAttribute Skill skill, Model model) {

        model.addAttribute("uid", uid.intValue());
        skill.setUserId(uid);
        String next = "";
        switch (action) {
            case ("add1"):
                skillRepository.save(skill);
                model.addAttribute(new Skill());
                next = "addskill";
                break;
            case ("submit"):
                skillRepository.save(skill);
                model.addAttribute(new Skill());
                next = "redirect:/info/" + uid;
                break;
            case ("skip"):
                next = "redirect:/info/" + uid;
                break;
        }
        return next;
    }
}
