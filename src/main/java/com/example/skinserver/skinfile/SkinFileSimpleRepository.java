package com.example.skinserver.skinfile;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by chanson.cc on 2018/4/17.
 */
public interface SkinFileSimpleRepository extends PagingAndSortingRepository<SkinFile, Long> {

    List<SkinFile> findBySkinOutputFileName(String skinName);

    Long deleteBySkinOutputFileName(String skinName);
}
