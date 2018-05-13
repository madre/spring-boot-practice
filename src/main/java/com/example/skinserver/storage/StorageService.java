package com.example.skinserver.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    void store(String directory, MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);
    Path load(String directory, String filename);

    Resource loadAsResource(String filename);
    Resource loadAsResource(String dir, String filename);

    void deleteAll();

}
