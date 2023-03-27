package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostImgService {

    @Value("${file.postImg}")
    private String uploadPath;

    @Transactional
    public void savePostImg(Long postId, MultipartFile[] uploadFiles) throws IOException {



    }
}
