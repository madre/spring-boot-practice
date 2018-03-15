package com.example.skinserver;

import com.example.skinserver.storage.QRCode;
import com.example.skinserver.storage.StorageFileNotFoundException;
import com.example.skinserver.storage.StorageService;
import com.google.zxing.WriterException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.Inet4Address;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
@EnableAutoConfiguration
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        log("listUploadedFiles:" + model);
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        Path zip2001 = storageService.load("2001.zip");
        if (zip2001.toFile().exists()) {
            Path qrcode = storageService.load("QRCode.png");
            log("qrcode:" + qrcode.toString());
            log("qrcode:" + qrcode.toUri());
            log("qrcode:" + qrcode.toAbsolutePath());
            String serveFile = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                    "serveFile", zip2001.getFileName().toString()).build().toString();
            log("serveFile:" + serveFile.toString());
            try {
                String ipAdress = Inet4Address.getLocalHost().getHostAddress();
                String url = "http://" + ipAdress + ":9898/" + "files/" + zip2001.getFileName();
                log("url:" + url);
                QRCode.createQRCode(url, qrcode.toString());
            } catch (WriterException e) {
                e.printStackTrace();
            }
            model.addAttribute("qrcode", "/files/" + qrcode.getFileName());
        }
        ResponseEntity<Resource> resourceResponseEntity = null;
        log("listUploadedFiles() returned: " + model);
        return "uploadForm";
    }

    public String getCurrentIpAddress() {

        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");

        HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
        return String.valueOf(request.getRequestURL());
    }

    @GetMapping("/upload")
    @ResponseBody
    public String listUploadedFiles() throws IOException {
        log("listUploadedFiles:");
        List<String> serveFile = storageService.loadAll().map(path
                ->
                MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());
        log("listUploadedFiles() returned: " + serveFile.size());

        return "uploadForm:" + serveFile.size();
    }

    public FileModel parseFile(String filename) {
        FileModel fileModel = new FileModel();
        Resource file = storageService.loadAsResource(filename);
        fileModel.name = file.getFilename();
//        fileModel.qrcode = QRCode.
        return fileModel;
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        log("serveFile:" + filename);
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        log("getOriginalFilename:" + file.getOriginalFilename());
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        if (file.getOriginalFilename().endsWith("zip")) {
            Path zipFilePath = storageService.load(file.getOriginalFilename());

            Path destFilePath = Paths.get("data-dir", "target.zip");
            try {
                Files.copy(zipFilePath, destFilePath, REPLACE_EXISTING);

                String cmd = "pwd";
                String result = getCmdOutput(cmd);
                log(result);
                cmd = "/Users/chanson.cc/00-work/02-需求文档/15-换肤/uitool/make.sh";
                result = getCmdOutput(cmd);
                log(result);
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
        return "redirect:/";
    }

    private static void unzip(Path zipFilePath, Path destDir) throws IOException {
        log("unzip() called with: zipFilePath = [" + zipFilePath + "], destDir = [" + destDir + "]");
        File dir = destDir.toFile();
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        //Get file entries
        ZipFile zipFile = new ZipFile(zipFilePath.toFile());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        FileSystem fileSystem = FileSystems.getDefault();
        //We will unzip files in this folder
        String uncompressedDirectory = destDir.toString();

        //Iterate over entries
        while (entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement();
            //If directory then create a new directory in uncompressed folder
            if (entry.isDirectory())
            {
                System.out.println("Creating Directory:" + uncompressedDirectory + entry.getName());
                Files.createDirectories(fileSystem.getPath(uncompressedDirectory, entry.getName()));
            }
            //Else create the file
            else
            {
                InputStream is = zipFile.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(is);
                String uncompressedFileName = uncompressedDirectory + entry.getName();
                Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                Files.createFile(uncompressedFilePath);
                FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                while (bis.available() > 0)
                {
                    fileOutput.write(bis.read());
                }
                fileOutput.close();
                System.out.println("Written :" + entry.getName());
            }
        }

    }
//    @GetMapping("/upload")
//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }


    @GetMapping("/sendfile")
    public String sendFile(RedirectAttributes redirectAttributes) throws IOException {
        Path buildinfo = Paths.get("images").resolve("buildinfo");
        File targetFile = new File(buildinfo.toUri());
        sendFileInternal(targetFile);
        Path dataZip = Paths.get("images").resolve("data.zip");
        File dataZipFile = new File(dataZip.toUri());
        sendFileInternal(dataZipFile);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + targetFile.getName() + "!" + dataZipFile.getName());
        return "redirect:/";
    }

    private void sendFileInternal(File file) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://100.85.115.70:9999/wifibook/");
            File targetFile = file;
            FileBody bin = new FileBody(targetFile);
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("file", targetFile, ContentType.TEXT_PLAIN, targetFile.getName())
                    .addPart("bin", bin)
                    .addPart("comment", comment)
                    .build();


            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private static void log(String msg) {
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
