package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.web.argumentresolver.LoginMemberArgumentResolver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ProfileImgInfo {
    private String rootImgDir;
    private String profImgDir;
    private String extension;
    private String saveName;
    private String fullProfPath;

    private ProfileImgInfo() {}
    private ProfileImgInfo(String rootImgDir, String extension) {
        this.rootImgDir = rootImgDir;
        this.profImgDir = generateProfImgDir(rootImgDir);
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

    public static ProfileImgInfo of(String rootImgDir, MultipartFile file) {
        String originName = file.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf(".") + 1);
        return new ProfileImgInfo(rootImgDir, extension);
    }

    public static ProfileImgInfo of(String rootImgDir, String url) {
        String extension = url.substring(url.lastIndexOf(".")+1);
        return new ProfileImgInfo(rootImgDir, extension);
    }

    public void mkProfImgDir() {
        new File(this.profImgDir).mkdir();
    }
}
