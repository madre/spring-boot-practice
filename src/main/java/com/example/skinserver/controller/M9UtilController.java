package com.example.skinserver.controller;

import com.example.skinserver.m9.M9Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class M9UtilController {

    @GetMapping("/m9util")
    public String m9util(Model model) {
        model.addAttribute("util", new UtilModel());
        return "m9util";
    }

    @PostMapping("/m9util/encode")
    public String m9EncodeSubmit(@ModelAttribute UtilModel util, Model model) {
        String content = util.getEncodeContent();
        String encode = M9Util.m9EncodeWithoutUrlEncode(content);
        util.setEncodeResult(encode);

        System.out.println("input  : " + content);
        System.out.println("encoded: " + encode);
        model.addAttribute("util", util);
        return "m9util";
    }

    @PostMapping("/m9util/decode")
    public String m9DecodeSubmit(@ModelAttribute UtilModel util, Model model) {
        String content = util.getDecodeContent();
        String decode = M9Util.m9decode(content);
        util.setDecodeResult(decode);

        System.out.println("input  : " + content);
        System.out.println("decoded: " + decode);
        model.addAttribute("util", util);
        return "m9util";
    }

}
