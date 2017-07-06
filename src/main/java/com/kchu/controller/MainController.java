package com.kchu.controller;

import com.kchu.models.*;
import com.kchu.repositorySQL.*;
import com.kchu.services.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/")
    public String goMain(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            model.addAttribute("message", "Welcome " + username);
        }
        else{
            model.addAttribute("message", "Welcome, Please Login");
        }
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


    @GetMapping("/register")
    public String createAccount(Model model){
        model.addAttribute(new User());
        return "createacc";
    }
    @PostMapping("/register")
    public String saveAccount(@Valid User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        userValidator.validate(user,result);
        if (result.hasErrors()){
            return "createacc";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setAuthority(authority);
        user.setEnabled(true);
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

    @RequestMapping("/myresume")
    public String seeUserResume()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer id =  userRepository.findIdByLogin(username);
        return "redirect:/info/" + id;
    }

    @GetMapping("/postjob")
    public String postJob(Model model)
    {
        model.addAttribute(new Job());
        return "addjobposting";
    }

    @PostMapping("/postjob")
    public String postJobSubmit(@Valid Job job,  BindingResult result, Model model)
    {
        if (result.hasErrors()){
            return "addjobposting";
        }
        model.addAttribute("job", job);
        model.addAttribute("message", "Job Added");
        jobRepository.save(job);
        notifyUsers(job.getId(), job.getSkillList());
        return "redirect:/";

    }
    //create notifications for users
    public void notifyUsers(int jobId, String skills){
        List<String> skillList = Arrays.asList(skills.split("\\s*,\\s*"));
        Set<Integer> resultSet = new HashSet<Integer>();
        for(String skillName : skillList){
            //Adds unique ids to result set
            Iterable<Integer> usersWSkill =  skillRepository.findIdByName(skillName);
                for(Integer userId : usersWSkill ){
                    resultSet.add(userId);
                }
            }
        //Creates notifications
        for(Integer userId : resultSet) {
            Notification notification = new Notification(userId.intValue(), jobId);
            notificationRepository.save(notification);
        }
    }

    @RequestMapping("/notifications")
    public String seeNotifications(Model model)
    {
        Integer id =  userRepository.findIdByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        Iterable<Notification> allNotifications = notificationRepository.findAllByUserId(id.intValue());
        ArrayList<Job> matchedJobs = new ArrayList<Job> ();
        for(Notification notification : allNotifications){
            matchedJobs.add(jobRepository.findFirstById(notification.getJobId()));
        }
        model.addAttribute("results", matchedJobs);
        return "notifications";

    }
}
