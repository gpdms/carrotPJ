package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostImg;
import com.exercise.carrotproject.domain.post.entity.PostImgEntityDtoMapper;
import com.exercise.carrotproject.domain.post.repository.PostImgRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl {

    @PersistenceContext
    EntityManager em;

    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;

    @Value("${file.postImg}")
    private String uploadPath;



//    @Override
    @Transactional
    public String insertPost(PostDto postDto, MultipartFile[] uploadFiles) throws IOException {
        log.info("uploadfiles-length {}", uploadFiles.length);
//        log.info("서비스단 postDto:",postDto);
        //Dto->Entity 변환
        Post postEntity = PostEntityDtoMapper.dtoToEntity(postDto);

        for(MultipartFile file : uploadFiles) {
            //1개이상 파일 올리고 && 이미지 타입이 아닐때 -> post에 저장하지 않는다.
            if ( !file.isEmpty() && file.getContentType().startsWith("image") == false) {
                return "이미지타입오류";
            }
        }
        //이미지 1개이상올리고 모두 이미지타입 / 이미지 0개
        //post에 insert
        em.persist(postEntity);

        for(MultipartFile file : uploadFiles) {
            //이미지 0개 -> post 이미지에 저장하지 않는다.
            if( file.isEmpty() ) {
                return "성공";
            }
        }
        //이미지에 insert
        insertPostImg(postEntity, uploadFiles);

        return "성공";
    }


//    @Override
    @Transactional
    public void insertPostImg(Post postEntity, MultipartFile[] uploadFiles) throws IOException {

        
        //사진 업로드
        List<PostImgDto> resultDTOList = new ArrayList<>();

        
        for(MultipartFile uploadFile: uploadFiles){

            //파일의 MIME 타입을 체크하여, 이미지 파일인지 여부를 확인하는 코드
            if(uploadFile.getContentType().startsWith("image")==false) {
                log.warn("this file is not image type");
            }
           

            //원본파일명
            String originalName = uploadFile.getOriginalFilename();
            log.info("originalName:"+originalName);
            //파일 이름과 확장자 추출. (마지막 디렉토리 구분자 다음에 오는 문자열 부분을 반환)
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);
            log.info("fileName:"+fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID는 128비트 숫자로 이루어진 고유한 식별자
            String uuid = UUID.randomUUID().toString();
            //UUID와 파일이름 결합
            String saveName = uuid+"_"+fileName;
            //파일을 저장할 경로
            Path savePath = Paths.get(uploadPath+ File.separator + folderPath + File.separator + saveName);

            //로컬에 사진저장
            uploadFile.transferTo(savePath);


            //데이터베이스에 사진정보 저장
            PostImg postImg = PostImg.builder()
                    .post(postEntity)
                    .orgName(originalName)
                    .savedName(saveName)
                    .savedPath(String.valueOf(savePath)).build();

            em.persist(postImg);

//            //썸네일 생성
//            String thumbnailName = uploadPath+File.separator+"s_"+uuid+"_"+fileName;
//            File thumbnailFile = new File(thumbnailName);
//            //썸네일 생성 라이브러리인 "Thumbnailator"를 사용하여 이미지 파일의 썸네일을 생성하는 메서드
//            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100); //100*100썸네일 만듦.
//            resultDTOList.add(new UploadResultDTO(fileName, uuid));
//            log.info(resultDTOList.toString());
            }
        }

    //날짜 폴더 생성하는 메소드
    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        // make folder ----
        File uploadPatheFolder = new File(uploadPath,folderPath);

        if(uploadPatheFolder.exists() == false){
            uploadPatheFolder.mkdirs();
        }

        return folderPath;
    }

//    @Override
    public List<PostDto> selectAllPost(){
        //JPQL
        String sql = "select p from Post p order by p.postId desc";
        List<Post> postEntityList = em.createQuery(sql, Post.class).getResultList();

        //Entity리스트 -> Dto 리스트
        List<PostDto> postDtoList = PostEntityDtoMapper.toDtoList(postEntityList);

        return  postDtoList;
    }


//    @Override
    public Page<PostDto> paging(List<PostDto> postList, Pageable pageable){

        //페이징
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postList.size());
        final Page<PostDto> page = new PageImpl<>(postList.subList(start, end), pageable, postList.size());
//        log.info("start:{}, end:{}, page:{}", start, end, page);
        return page;
    }


//    @Override
    public PostDto selectOnePost(Long postId){
        Post postEntity = em.find(Post.class, postId);

        //Entity -> Dto 변환
        PostDto postDto = PostEntityDtoMapper.entityToDto(postEntity);

        return postDto;
    }

//    @Override
    public List<PostImgDto> selectPostImgs(Long postId){

        String sql = "select i from PostImg i where i.post.postId = :postId";
        List<PostImg> postImgList = em.createQuery(sql, PostImg.class)
                                    .setParameter("postId", postId)
                                    .getResultList();

        //Entity리스트 -> Dto 리스트
        List<PostImgDto> postImgDtoList = PostImgEntityDtoMapper.toDtoList(postImgList);

        return postImgDtoList;
    }
//    @Override
    public PostImgDto selectOnePostImg(Long imgId){
        PostImg imgEntity = postImgRepository.findById(imgId).orElse(null);
        //entity->dto
        PostImgDto imgDto = PostImgEntityDtoMapper.entityToDto(imgEntity);

        return imgDto;
    }

    public void deletePost(Long postId){
        Post post = postRepository.findById(postId).orElse(null);

    }

    















}
