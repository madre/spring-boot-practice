package com.example.skinserver.controller;

import com.example.skinserver.m9.M9Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class M9UtilController {

    @GetMapping("/util")
    public String m9util(Model model) {
        model.addAttribute("greet", new UtilModel());
        return "m9util";
    }

    @PostMapping("/util")
    public String m9submit(@ModelAttribute UtilModel util, Model model) {
        String content = util.getContent();
        String encode = M9Util.m9Encode(content);
        util.setContent(encode);

        System.out.println("input  : " + content);
        System.out.println("decoded: " + encode);
        model.addAttribute("greet", util);
        return "encodeResult";
    }

    @GetMapping("/greeting2")
    public String greetingForm(Model model) {
        model.addAttribute("greet", new Greeting2());
        return "m9util";
    }

    @PostMapping("/greeting2")
    public String m9submit(@ModelAttribute Greeting2 greet, Model model) {
        model.addAttribute("greet", greet);
        return "encodeResult";
    }
}
