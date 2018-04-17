package com.example.skinserver.controller;

import com.example.skinserver.mysql.User;
import com.example.skinserver.mysql.UserRepository;
import com.example.skinserver.mysql.UserSimpleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by chanson.cc on 2018/4/17.
 */
@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSimpleRepository userSimpleRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "user_index";
    }

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewUser(String name, String email, String gender) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setGender(gender);
        userRepository.save(user);
        return "Saved";
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "Saved";
    }

    @GetMapping(path = "/query")
    public @ResponseBody
    Iterable<User> query(@ModelAttribute User user) {
        List<User> all = userSimpleRepository.findByName(user.getName());
        return all;
    }

    @GetMapping(path = "/delete")
    public @ResponseBody
    Long delete(@ModelAttribute User user) {
        List<User> all = userSimpleRepository.findByName(user.getName());
        if (all.isEmpty()) {
            return 0l;
        } else {
            long id = all.get(0).getId();
            userSimpleRepository.deleteById(id);
            return id;
        }
    }


    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
