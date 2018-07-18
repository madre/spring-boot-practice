package com.example.skinserver.shuqi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chanson.cc on 2018/7/18.
 */
@Controller
@RequestMapping(path = "/shuqi")
public class OpenProtocolController {

    @GetMapping("openprotocol")
    public String index(Model model) {
        return "open_protocol";
    }

}
