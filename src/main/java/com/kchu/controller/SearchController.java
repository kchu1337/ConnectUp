package com.kchu.controller;
import com.kchu.models.*;
import com.kchu.repositorySQL.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class SearchController {

    @Autowired
    private EduRepository eduRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private SkillRepository skillRepository;

    @GetMapping("/search")
    public String doSearch(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role= auth.getAuthorities().toString();
        Integer id =  userRepository.findIdByLogin(username);
        model.addAttribute("role", role.toLowerCase());
        model.addAttribute(new Search());
        return "search";
    }

    @PostMapping("/search")
    public String searchResult(@ModelAttribute Search search, BindingResult bindingResult, Model model){
        model.addAttribute(search);

        //Creates a list of all users
        ArrayList<Integer> results =(ArrayList<Integer>) userRepository.findId();


        //Finds the intersection of users and users matching the search conditions
        if (!search.getName().isEmpty()){
            ArrayList<Integer> temp = (ArrayList<Integer>)  userRepository.findIdByName(search.getName());
            results.retainAll(temp);
        }
        
        if (search.getCollege() != null && !search.getCollege().isEmpty()){
            ArrayList<Integer> temp = (ArrayList<Integer>)  eduRepository.findIdByCollege(search.getCollege());
            results.retainAll(temp);
        }
        
        if (search.getCompany()!= null && !search.getCompany().isEmpty()){
            ArrayList<Integer> temp = (ArrayList<Integer>)  workRepository.findIdByCompany(search.getCompany());
            results.retainAll(temp);
        }
        
        if (search.getSkill()!= null && !search.getSkill().isEmpty()){
            ArrayList<Integer> temp = (ArrayList<Integer>)  skillRepository.findIdByName(search.getSkill());
            results.retainAll(temp);
        }

        //finds the list of names corresponding to the ids
        ArrayList<User> resultUsers = new ArrayList<User>();
        for(Integer userId:results){
           resultUsers.add(userRepository.findFirstById(userId.intValue()));
        }
        model.addAttribute("results",resultUsers);
        return "search";
    }


}
