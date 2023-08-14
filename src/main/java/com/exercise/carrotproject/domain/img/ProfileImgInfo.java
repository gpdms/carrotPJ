package com.exercise.carrotproject.domain.img;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ProfileImgInfo {
    @Value("${file.postImg}")
    private String rootImgDir;
    private final String profImgDir;
    private final String extension;
    private final String saveName;
    private final String fullProfPath;

    private ProfileImgInfo(String extension) {
        this.profImgDir = generateProfImgDir(this.rootImgDir);
        this.extension = extension;
        this.saveName = UUID.randomUUID() + "." + this.extension;
        this.fullProfPath = this.profImgDir + File.separator + this.saveName;
    }

    private String generateProfImgDir(String rootImgDir) {
        StringBuilder builder = new StringBuilder(rootImgDir);
        builder.append(File.separator);
        builder.append("member");
        builder.append(File.separator);
        builder.append(LocalDate.now());
        return builder.toString();
    }

    public static ProfileImgInfo of(MultipartFile file) {
        String originName = file.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf(".") + 1);
        return new ProfileImgInfo(extension);
    }

    public static ProfileImgInfo of(String url) {
        String extension = url.substring(url.lastIndexOf(".")+1);
        return new ProfileImgInfo(extension);
    }

    public void mkProfImgDir() {
        new File(this.profImgDir).mkdir();
    }
}
