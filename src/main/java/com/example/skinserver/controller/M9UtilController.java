package com.example.skinserver.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chanson.cc on 2018/4/15.
 */

@RestController
@EnableAutoConfiguration
public class M9UtilController {
    @RequestMapping("/m9util")
    public String m9util(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
