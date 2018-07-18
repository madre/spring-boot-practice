package com.example.skinserver.shuqi;

import com.example.skinserver.controller.Greeting;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

/**
 * Created by chanson.cc on 2018/7/18.
 */
@Controller
@RequestMapping(path = "/shuqi")
public class AntiReplaceController {


    @GetMapping("/antireplace")
    public String selectGet(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "shuqi_select_anti_replace";
    }

    @PostMapping("/antireplace")
    @ResponseBody
    public ResponseEntity<InputStreamResource> selectPost(@ModelAttribute AntiReplaceModel antiReplaceModel,
                      @RequestParam("placeId") String placeId,
                      @RequestParam("fileName") String fileName,
                      Model model) throws IOException {
        String result = placeId + fileName;
//        return antiReplaceModel.getPlaceId() + "_"+antiReplaceModel.getFilename() + "_"+ result;

        File antiFile = new File(fileName);
        // 创建文件
        try {
            if (antiFile.exists()) {
                antiFile.delete();
            }
            antiFile.createNewFile();
            // creates a FileWriter Object
            FileWriter writer = new FileWriter(antiFile);
            // 向文件写入内容
            writer.write("placeid = " + placeId + "\n");
            writer.flush();
            writer.close();
            //创建 FileReader 对象
            FileReader fr = new FileReader(antiFile);
            char[] a = new char[50];
            fr.read(a); // 从数组中读取内容
            for (char c : a)
                System.out.print(c); // 一个个打印字符
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Resource resource = new UrlResource(antiFile.toPath().toUri());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE))
                .contentLength(resource.contentLength())
                .body(new InputStreamResource(java.nio.file.Files.newInputStream(resource.getFile().toPath(), StandardOpenOption.READ)));
    }

}
