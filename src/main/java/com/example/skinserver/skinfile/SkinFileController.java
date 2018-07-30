package com.example.skinserver.skinfile;

import com.example.skinserver.config.ConfigBean;
import com.example.skinserver.storage.QRCode;
import com.example.skinserver.storage.StorageService;
import com.google.common.io.Files;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by chanson.cc on 2018/5/5.
 */
@Controller
@RequestMapping(path = "/skinFile")
public class SkinFileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkinFileController.class);
    public static final String DIR_NAME = "skin_file_dir";

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ConfigBean configBean;
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
    String addNewSkinFile(@ModelAttribute SkinFile skinFile) {
        skinFileRepository.save(skinFile);
        return "Saved";
    }

    @GetMapping(path = "/query")
    public @ResponseBody
    Iterable<SkinFile> query(@ModelAttribute SkinFile skinFile) {
        List<SkinFile> all = skinFileSimpleRepository.findBySkinOutputFileName(skinFile.getSkinOutputFileName());
        return all;
    }

    @GetMapping(path = "/delete")
    public
    String delete(@ModelAttribute SkinFile skinFile) {
        if (skinFile.getId() > 0) {
            long id = skinFile.getId();
            skinFileSimpleRepository.deleteById(id);
        } else {
            List<SkinFile> all = skinFileSimpleRepository.findBySkinOutputFileName(skinFile.getSkinOutputFileName());
            if (all.isEmpty()) {
            } else {
                long id = all.get(0).getId();
                skinFileSimpleRepository.deleteById(id);
            }
        }

        return "redirect:/skinFile/all";
    }

    @GetMapping(path = "/all")
    public
    String getAllSkinFiles(Model model) {
//        Sort.Order idOrder = new Sort.Order(Sort.Direction.DESC, "id");
//        Sort.Order nameOrder = new Sort.Order(Sort.Direction.ASC,"name");
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        model.addAttribute("skinFiles", skinFileSimpleRepository.findAll(sort));
        return "skinfile_all";
    }


    @PostMapping("/upload")
    public
    String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Model model) {
        String fileName = file.getOriginalFilename();

        String fileNameWithoutExtension = Files.getNameWithoutExtension(fileName);;
        String skinName = "aaaa";
        String skinId = "id";
        String zip = "";
        String qrcode = "";
        storageService.store(DIR_NAME, file);


        if (file.getOriginalFilename().endsWith("zip")) {
            Path zipFilePath = storageService.load(DIR_NAME, file.getOriginalFilename());

            String time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
            String sourceName = time + "_" + fileNameWithoutExtension;
            Path destFilePath = Paths.get("bash", "data-dir", sourceName + ".zip");
            try {
                java.nio.file.Files.copy(zipFilePath, destFilePath, REPLACE_EXISTING);

                String cmd = "pwd";
                CmdResult cmdResult = getCmdOutput(cmd);
                LOGGER.debug(cmdResult.result);
                String relativePath  = "bash/data-dir/" + sourceName + ".zip";
                cmd = String.format("./bash/copyToSkinBuilder.sh");
                cmdResult = getCmdOutput("bash", cmd, relativePath, sourceName);
                LOGGER.debug(cmdResult.result);
                model.addAttribute("result", cmdResult.result);
                if (cmdResult.isSucc()) {

                } else {
                    return "skinfile_result";
                }
//                Path buildinfo = Paths.get("images").resolve("buildinfo");
//                sendFileInternal(buildinfo.toFile());
//                Path dataZip = Paths.get("images").resolve("2001.zip");
//                sendFileInternal(dataZip.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                String ipAdress = configBean.getIpAddress();
                if (StringUtils.isEmpty(ipAdress)) {
                    ipAdress = Inet4Address.getLocalHost().getHostAddress();
                }
                String url = "http://" + ipAdress + ":9898" + "/skinFile/skinOutput/" + destFilePath.getFileName();
                LOGGER.debug("url:" + url);


                Path qrcodeFile = storageService.load("skinOutput", sourceName + "_QRCode.png");
                QRCode.createQRCode(url, qrcodeFile.toString());
                qrcode = qrcodeFile.toString();
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SkinFile skinFile = new SkinFile();
            skinFile.setOriginFileName(fileName);
            skinFile.setQrcode(qrcode);
            skinFile.setZip(zipFilePath.toString());
            skinFile.setSkinOutputFileName(sourceName + ".zip");

            skinFileRepository.save(skinFile);

            return "redirect:/skinFile/all";
        } else {
            return "redirect:/skinFile/all";
        }
    }


    private CmdResult getCmdOutput(String... cmd) throws IOException, InterruptedException {
        for (int i = 0; i < cmd.length; i++) {
            System.out.println("cmd:" + cmd[i]);
        }
//        Process ps = Runtime.getRuntime().exec(cmd);
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);

        Process ps = processBuilder.start();
        int exitCode = ps.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        CmdResult cmdResult = new CmdResult();
        cmdResult.exitCode = exitCode;
        cmdResult.result = sb.toString();
        System.out.println(":exitCode: " + cmdResult.exitCode);
        System.out.println(":result:" + cmdResult.result);
        return cmdResult;
    }

    @GetMapping("/skinOutput/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> skinOutputFile(@PathVariable String fileName) throws IOException {
        return downloadOutpufFile(fileName);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadOutpufFile(@RequestParam("fileName") String fileName) throws IOException {

        LOGGER.debug("fileName: " + fileName);
        fileName = URLDecoder.decode(fileName,  "utf-8");
        LOGGER.debug("fileName decoded: " + fileName);
        String mineType = servletContext.getMimeType(fileName);
        MediaType mediaType = MediaType.parseMediaType(mineType);
        LOGGER.debug("mediaType:"+ mediaType);
        // pay attention to @PathVariable and difference between fileName and filename
        Resource file = storageService.loadAsResource("skinOutput", fileName);

        String retFileName = file.getFilename();
        if (fileName.endsWith(".zip")) {
            List<SkinFile> all = skinFileSimpleRepository.findBySkinOutputFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            if (!all.isEmpty()) {
                String id = String.valueOf(all.get(0).getId());
                LOGGER.debug("Id:" + id);
                retFileName = id + ".zip";
            }
        } else {
            retFileName = file.getFilename();
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + retFileName)
                .contentType(mediaType)
                .contentLength(file.contentLength())
                .body(new InputStreamResource(java.nio.file.Files.newInputStream(file.getFile().toPath(), StandardOpenOption.READ)));
    }

    private static class CmdResult {
        public int exitCode;
        public String result;

        public boolean isSucc() {
            return exitCode == 0;
        }
    }
}
