package com.example.skinserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String m9submit(@ModelAttribute Greeting greeting) {
        return "result";
    }


    @GetMapping("/greeting2")
    public String greeting2Form(Model model) {
        model.addAttribute("greet", new Greeting2());
        return "m9util";
    }

    @PostMapping("/greeting2")
    public String m9submit(@ModelAttribute Greeting2 greet, Model model) {
        model.addAttribute("greet", greet);
        return "m9utilResult";
    }

}
