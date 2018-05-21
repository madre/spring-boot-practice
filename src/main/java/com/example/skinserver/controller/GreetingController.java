package com.example.skinserver.controller;

import com.example.skinserver.config.ConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {
    private final Logger logger = LoggerFactory.getLogger(GreetingController.class);


    @Value("${local.ipAddress}")
    private String ipAddress;

    @Value("${local.name}")
    private String name;

    @Autowired
    private ConfigBean configBean;

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        Greeting greeting = new Greeting();
        greeting.setContent(ipAddress + "_" + name);
        model.addAttribute("greeting", greeting);
        logger.debug("========>>>>>>>>>");
        logger.debug(greeting.getContent());
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        return "greeting";
    }

    @PostMapping("/greeting")
    public String m9submit(@ModelAttribute Greeting greeting) {
        return "result";
    }


    @GetMapping("/greeting2")
    public String greeting2Form(Model model) {
        Greeting2 greeting = new Greeting2();
        greeting.setContent(configBean.getIpAddress() + "_" + configBean.getName());
        model.addAttribute("greeting", greeting);
        return "m9util";
    }

    @PostMapping("/greeting2")
    public String m9submit(@ModelAttribute Greeting2 greet, Model model) {
        model.addAttribute("greet", greet);
        return "m9utilResult";
    }

}
