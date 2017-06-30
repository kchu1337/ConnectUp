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

import javax.servlet.http.HttpServletRequest;

/**
 * Created by student on 6/28/17.
 */
@Controller
@SessionAttributes("loginId")
public class MainController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EduRepository eduRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("/")
    public String goMain()
    {
        return "index";
    }

    @RequestMapping("/add")
    public String addResume(Model model){
        model.addAttribute(new Education());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer id =  userRepository.findIdByLogin(username);
        model.addAttribute("id", id.intValue());
        return "redirect:/addedu/" +id;
    }


    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/create")
    public String createAccount(Model model){
        model.addAttribute(new User());
        return "createacc";
    }
    @PostMapping("/create")
    public String saveAccount(@ModelAttribute User user, Model model){
        model.addAttribute(new User());
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("/info/{uid}")
    public String seeInfo(@PathVariable("uid")int userId, Model model){
        model.addAttribute("user",userRepository.findFirstById(userId));
        model.addAttribute("education",eduRepository.findAllByUserId(userId));
        model.addAttribute("work",workRepository.findAllByUserId(userId));
        model.addAttribute("skills",skillRepository.findAllByUserId(userId));
        return "info";
    }

    @RequestMapping("/current")
    public String seeUserResume()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer id =  userRepository.findIdByLogin(username);
        return "redirect:/info/" + id;
    }

}
