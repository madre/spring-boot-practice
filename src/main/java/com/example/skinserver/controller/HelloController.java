package com.example.skinserver.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@EnableAutoConfiguration
public class HelloController {

//    @RequestMapping(name = "/hello", method = {RequestMethod.GET})
//    public String index() {
//        System.out.println("Welcome to hello controller");
//
//        String cmd = "pwd";
//        try {
//            String result = getCmdOutput(cmd);
//
//
//            cmd = "/Users/chanson.cc/00-work/02-需求文档/15-换肤/uitool/make.sh";
//            result = getCmdOutput(cmd);
//
//            return result;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return "Hello world1 from HelloController";
//    }

    private String getCmdOutput(String cmd) throws IOException, InterruptedException {
        System.out.println("cmd:" + cmd);
        Process ps = Runtime.getRuntime().exec(cmd);
        ps.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String result = sb.toString();
        System.out.println(result);
        return result;
    }
}
