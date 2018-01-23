package com.example.skinserver.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.function.Consumer;

@RestController
@EnableAutoConfiguration
public class SkinController {

    @RequestMapping("/skinfile")
    public String login(HttpServletRequest request) {
        String method = request.getMethod();
        log("method:" + method);
        Enumeration<String> headerNames = request.getHeaderNames();

        log("==========");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            log("header:" + header);
        }

        log("==========");
        try {
            Collection<Part> parts = request.getParts();
            parts.forEach(new Consumer<Part>() {
                              @Override
                              public void accept(Part part) {
                                  log("part:" + part);
                              }
                          }
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        log("==========");
        String name = request.getParameter("name");
        String size = request.getParameter("size");

        System.out.println("Welcome to skinfile controller");
        log("name" + name);
        log("size" + size);

        return "Hello world1 from HelloCSkinControllerontroller" + name + size;
    }

    private void log(String msg) {
        System.out.println(msg);
    }

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
