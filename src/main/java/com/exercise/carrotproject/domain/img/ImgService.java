package com.exercise.carrotproject.domain.img;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class ImgService {
    //이미지 가공
    public String checkImg(String rootPath, MultipartFile img) {
        if (img == null) {
            return null;
        }
        if (img.getContentType().startsWith("image") == false) {
            log.warn("this file is not image type");
            return null;
        }

        //디렉토리 생성
        makeFolder(rootPath);
        return null;
    }

    //날짜 폴더 생성
    private String makeFolder(String rootPath) {
        String folderName= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFolderPath = folderName.replace("/", File.separator);

        File imgDirPath = new File(rootPath,newFolderPath);
        if(imgDirPath.exists() == false){
            imgDirPath.mkdirs();
        }
        log.info("fullPath {}", imgDirPath.getPath());
        return imgDirPath.getPath();
    }

    //이미지 path 생성
    private Map<String, String> makePath(MultipartFile img, String imgDirPath) {
        Map<String, String> names = new HashMap<>();
        String origin_name = img.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origin_name.substring(origin_name.lastIndexOf("."));
        String save_name = uuid + extension;
        String save_path = imgDirPath + "/" + save_name;
        return names;
    }

    //서버에 이미지 저장
    public void saveImgServer(MultipartFile img, String save_path){
        try {
            img.transferTo(new File(save_path));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

}
