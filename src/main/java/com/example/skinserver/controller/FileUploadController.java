package com.example.skinserver.controller;

import com.example.skinserver.storage.FileModel;
import com.example.skinserver.storage.StorageFileNotFoundException;
import com.example.skinserver.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chanson.cc on 2018/4/21.
 */
@Controller
@EnableAutoConfiguration
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files")
    public String listUploadedFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        List<FileModel> fileModelList = storageService.loadAll().map(
                path -> new FileModel(path.getFileName().toString(), MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString()))
                .collect(Collectors.toList());
        fileModelList.forEach(item -> item.crateQrcode(storageService));
        model.addAttribute("fileModelList", fileModelList);

        return "files_index";
    }

    @GetMapping("/aaa/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        // pay attention to @PathVariable and difference between fileName and filename
        log("filename:" + fileName);
        Resource file = storageService.loadAsResource(fileName);
        log("file:" + file);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("skinName") String skinName,
                                   @RequestParam("select") String select,

                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!" + "with name:" + skinName + ", select:" + select);
        redirectAttributes.addFlashAttribute("skinName", skinName);
        redirectAttributes.addFlashAttribute("select", select);
        return "redirect:/files";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


    private static void log(String msg) {
        System.out.println(msg);
    }

    @RequestMapping(value="/upload-dir/{fileName:.+}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<InputStreamResource> uploadedFile(@PathVariable String fileName) throws IOException {
        Resource file = storageService.loadAsResource(fileName);
        return ResponseEntity
                .ok()
                .contentType(
                        MediaType.parseMediaType(
                                URLConnection.guessContentTypeFromName(fileName)
                        )
                )
                .body(new InputStreamResource(
                        Files.newInputStream(file.getFile().toPath(), StandardOpenOption.READ))
                );
    }
}
