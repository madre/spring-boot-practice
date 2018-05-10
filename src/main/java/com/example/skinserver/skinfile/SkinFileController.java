package com.example.skinserver.skinfile;

import com.example.skinserver.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by chanson.cc on 2018/5/5.
 */
@Controller
@RequestMapping(path = "/skinFile")
public class SkinFileController {
    private static final String DIR_NAME = "skin_file_dir";
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
        return "uploaded:" + fileName;
    }
}
