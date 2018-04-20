package com.example.skinserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SelectController {

    @GetMapping("/select")
    public String selectGet(Model model) {
        model.addAttribute("greeting", new Greeting());
        SelectModel selectModel = new SelectModel();
        selectModel.getList().add(new SelectModel.Country("1","aaaa"));
        selectModel.getList().add(new SelectModel.Country("2","bbbb"));
        selectModel.getList().add(new SelectModel.Country("3","cccc"));
        selectModel.getList().add(new SelectModel.Country("4","dddd"));
        model.addAttribute("select", selectModel);
        return "select";
    }

    @PostMapping("/select")
    public @ResponseBody String selectPost(@ModelAttribute SelectModel selectModel, Model model) {
        return selectModel.getContent();
    }

}
