package com.example.skinserver.controller;

import com.example.skinserver.mysql.User;
import com.example.skinserver.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chanson.cc on 2018/4/17.
 */
@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private UserSimpleRepository userSimpleRepository = new UserSimpleRepository();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "user_index";
    }

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(String name, String email, String gender) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setGender(gender);
        userRepository.save(user);
        return "Saved";
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "Saved";
    }

//    @PostMapping(path = "/query")
//    public @ResponseBody Iterable<User> query(@ModelAttribute User user) {
//        List<User> all = userSimpleRepository.findAll(new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root = query.from(User.class);
//                Path<String> nameExp = root.get("name");
//                return criteriaBuilder.like(nameExp, "%" + user.getName() + "%");
//            }
//        });
//        return all;
//    }


    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
