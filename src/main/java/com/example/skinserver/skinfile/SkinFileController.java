package com.example.skinserver.skinfile;

import com.example.skinserver.SkinserverApplication;
import com.example.skinserver.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by chanson.cc on 2018/5/5.
 */
@Controller
@RequestMapping(path = "/skinFile")
public class SkinFileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkinserverApplication.class);
    public static final String DIR_NAME = "skin_file_dir";
    @Autowired
    private SkinFileRepository skinFileRepository;
    @Autowired
    private SkinFileSimpleRepository skinFileSimpleRepository;
    private final StorageService storageService;

    @Autowired
    public SkinFileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("")
    public String index1(Model model) {
        model.addAttribute("skinFile", new SkinFile());
        return "skinfile_index";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("skinFile", new SkinFile());
        return "skinfile_index";
    }


    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@ModelAttribute SkinFile skinFile) {
        skinFileRepository.save(skinFile);
        return "Saved";
    }

    @GetMapping(path = "/query")
    public @ResponseBody
    Iterable<SkinFile> query(@ModelAttribute SkinFile skinFile) {
        List<SkinFile> all = skinFileSimpleRepository.findBySkinName(skinFile.getSkinName());
        return all;
    }

    @GetMapping(path = "/delete")
    public @ResponseBody
    Long delete(@ModelAttribute SkinFile skinFile) {
        List<SkinFile> all = skinFileSimpleRepository.findBySkinName(skinFile.getSkinName());
        if (all.isEmpty()) {
            return 0l;
        } else {
            long id = all.get(0).getId();
            skinFileSimpleRepository.deleteById(id);
            return id;
        }
    }

    @GetMapping(path = "/all")
    public
    String getAllSkinFiles(Model model) {
        model.addAttribute("skinFiles", skinFileRepository.findAll());
        return "skinfile_all";
    }


    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String fileName = file.getOriginalFilename();
        String skinName = "aaaa";
        String skinId = "id";
        String zip = "";
        String qrcode = "";
        storageService.store(DIR_NAME, file);


        if (file.getOriginalFilename().endsWith("zip")) {
            Path zipFilePath = storageService.load(DIR_NAME, file.getOriginalFilename());

            Path destFilePath = Paths.get("bash", "data-dir", "target.zip");
            try {
                Files.copy(zipFilePath, destFilePath, REPLACE_EXISTING);

                String cmd = "pwd";
                String result = getCmdOutput(cmd);
                LOGGER.debug(result);
                cmd = "bash ./bash/copyToSkinBuilder.sh";
                result = getCmdOutput(cmd);
                LOGGER.debug(result);
//                Path buildinfo = Paths.get("images").resolve("buildinfo");
//                sendFileInternal(buildinfo.toFile());
//                Path dataZip = Paths.get("images").resolve("2001.zip");
//                sendFileInternal(dataZip.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return "uploaded:" + fileName;
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
