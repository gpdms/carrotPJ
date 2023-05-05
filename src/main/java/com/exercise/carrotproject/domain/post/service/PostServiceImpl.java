package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.entity.QChat;
import com.exercise.carrotproject.domain.chat.entity.QChatRoom;
import com.exercise.carrotproject.domain.enumList.*;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.post.dto.MtPlaceDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.*;
import com.exercise.carrotproject.domain.post.repository.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService{

    @PersistenceContext
    EntityManager em;

    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;
    private final MemberRepository memberRepository;
    private final MtPlaceRepository mtPlaceRepository;
    private final WishRepository wishRepository;
    private final JPAQueryFactory jpaQueryFactory; //QuerydslConfig파일에 bean등록함
//    private final CustomPostRepositoryImpl customPostRepository;

    @Value("${file.postImg}")
    private String uploadPath;


    @Override
    @Transactional
    public String insertPost(PostDto postDto, MultipartFile[] uploadFiles, MtPlaceDto mtPlaceDto) throws IOException {
        //Dto->Entity 변환
        Post postEntity = PostEntityDtoMapper.dtoToEntity(postDto);

        //거래희망장소 테이블에 insert
        if(mtPlaceDto.getLat() != null && mtPlaceDto.getLon() != null && mtPlaceDto.getPlaceInfo()!=null){
            insertMtPlace(postEntity, mtPlaceDto);
        }

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
        //이미지 테이블에 insert
        insertPostImg(postEntity, uploadFiles);

        return "성공";
    }


    @Override
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
            //파일 이름과 확장자 추출. (마지막 디렉토리 구분자 다음에 오는 문자열 부분을 반환)
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

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

    //거래희망장소 insert
    @Override
    @Transactional
    public void insertMtPlace(Post postEntity, MtPlaceDto mtPlaceDto){
        MtPlace mtPlaceEntity = MtPlace.builder()
                .post(postEntity)
                .lat(mtPlaceDto.getLat())
                .lon(mtPlaceDto.getLon())
                .placeInfo(mtPlaceDto.getPlaceInfo())
                .build();
        mtPlaceRepository.save(mtPlaceEntity);
    }

    //거래희망장소 update
    @Override
    @Transactional
    public void updateMtPlace(PostDto postDto, MtPlaceDto mtPlaceDto){
        Post post = PostEntityDtoMapper.dtoToEntity(postDto);
        MtPlace mtPlace= mtPlaceRepository.findByPost(post);

        if (mtPlaceDto.getLat() == 0 || mtPlaceDto.getLon()==0 || mtPlaceDto.getPlaceInfo()==""){
            deleteMtPlace(post);
        } else if(mtPlace == null){
            insertMtPlace(post, mtPlaceDto);
        } else{
            QMtPlace qMtPlace = QMtPlace.mtPlace;
            jpaQueryFactory.update(qMtPlace)
                    .set(qMtPlace.lat, mtPlaceDto.getLat())
                    .set(qMtPlace.lon, mtPlaceDto.getLon())
                    .set(qMtPlace.placeInfo, mtPlaceDto.getPlaceInfo())
                    .where(qMtPlace.mtPlaceId.eq(mtPlace.getMtPlaceId()))
                    .execute();
        }
    }

    //거래희망장소 delete
    @Override
    @Transactional
    public void deleteMtPlace(Post post){
        mtPlaceRepository.deleteByPost(post);
    }


    @Override
    public List<PostDto> selectAllPost(MemberDto memberDto){
        String loginMemId = null;
        Loc loginMemLoc = null;
        if(memberDto != null) {
            loginMemId = memberDto.getMemId();
            loginMemLoc = memberDto.getLoc();
        }

        List<Post> postEntityList = postRepository.selectBoardPost(loginMemId, loginMemLoc);

        //Entity리스트 -> Dto 리스트
        List<PostDto> postDtoList = PostEntityDtoMapper.toDtoList(postEntityList);

        return  postDtoList;
    }


    @Override
    public Page<PostDto> paging(List<PostDto> postList, Pageable pageable){

        //페이징
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postList.size());
        final Page<PostDto> page = new PageImpl<>(postList.subList(start, end), pageable, postList.size());

        return page;
    }


    @Override
    public PostDto selectOnePost(Long postId){
        Post postEntity = em.find(Post.class, postId);

        //Entity -> Dto 변환
        PostDto postDto = PostEntityDtoMapper.entityToDto(postEntity);

        return postDto;
    }

    //게시글의 모든 이미지 반환
    @Override
    public List<PostImgDto> selectPostImgs(Long postId){

        String jpql = "select i from PostImg i where i.post.postId = :postId";
        List<PostImg> postImgList = em.createQuery(jpql, PostImg.class)
                                    .setParameter("postId", postId)
                                    .getResultList();

        //Entity리스트 -> Dto 리스트
        List<PostImgDto> postImgDtoList = PostImgEntityDtoMapper.toDtoList(postImgList);

        return postImgDtoList;
    }
    
    //게시글의 첫번째 이미지 반환
    @Override
    public PostImgDto selectOnePostImg(Long imgId){
        PostImg imgEntity = postImgRepository.findById(imgId).orElse(null);
        //entity->dto
        PostImgDto imgDto = PostImgEntityDtoMapper.entityToDto(imgEntity);

        return imgDto;
    }


    @Override
    public MtPlaceDto selectMtPlace(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        MtPlace mtPlace = mtPlaceRepository.findByPost(post);
        if(mtPlace == null){
            return null;
        }
        //Entity->Dto
        MtPlaceDto mtPlaceDto = MtPlaceEntityDtoMapper.entityToDto(mtPlace);

        return mtPlaceDto;
    }

    //게시글 이미지 아이디로 삭제
    @Override
    @Transactional
    public void deleteOnePostImg(Long imgId){
        postImgRepository.deleteById(imgId);
    }

    

    //게시글 업데이트
    @Override
    @Transactional
    public void updatePost(PostDto postDto, MultipartFile[] uploadFiles) throws IOException {
        //Dto -> Enity
        Post post = PostEntityDtoMapper.dtoToEntity(postDto);

        Post rs = postRepository.save(post);

        //파일이 비어있을 경우
        for(MultipartFile file : uploadFiles) {
            //이미지 0개 -> post 이미지에 저장하지 않는다.
            if( file.isEmpty() ) {
                return; //이미지추가 안함
            }
        }

        //새이미지 추가
        insertPostImg(post, uploadFiles);
    }



    //게시글 삭제
    @Override
    @Transactional
    public void deletePost(Long postId){
        postRepository.deleteById(postId);

    }

    
    //hideState 변경
    @Override
    @Transactional
    public String updateHideState(Long postId, String hideStateName){

        HideState hideState = null;
        String msg = null;
        if (hideStateName.equals("보임")){
            hideState = HideState.HIDE;
            msg = "게시물이 이웃에게 보이지 않게 숨깁니다.";
        } else if (hideStateName.equals("숨김")){
            hideState = HideState.SHOW;
            msg = "게시물이 이웃에게 다시 보입니다.";
        } else{
            hideState = null;
            msg=null;
        }

        QPost qpost = QPost.post;

        long resultCount = jpaQueryFactory
                .update(qpost)
                .set(qpost.hideState, hideState)
                .where(qpost.postId.eq(postId))
                .execute();

//        log.info("update hideState 결과>>>>>>>>>>{}", resultCount);
        return msg;

    }


    //sellState변경
    @Override
    @Transactional
    public String updateSellState(Long postId, String sellStateName){

        SellState sellState = null;
        String msg = null;

        if(sellStateName.equals("판매중")){
            sellState = SellState.ON_SALE;
            msg ="판매중";
        }else if(sellStateName.equals("예약중")){
            sellState = SellState.RESERVATION;
            msg ="예약중";

        } else if (sellStateName.equals("판매완료")) {
            sellState = SellState.SOLD;
            msg ="판매완료";
        } else{
            sellState = null;
        }

        QPost qpost = QPost.post;

        jpaQueryFactory
                .update(qpost)
                .set(qpost.sellState, sellState)
                .where(qpost.postId.eq(postId))
                .execute();

        return msg;
    }

    //판매여부로 게시글들 반환
    @Override
    public Map selectPostBySellState(String memId){
        Member member = memberRepository.findById(memId).orElseThrow();

        //판매중,예약중
        List<Post> onSalePostList = postRepository.findByMemberAndHideStateAndSellStateOrSellStateOrderByPostIdDesc(member, HideState.SHOW, SellState.ON_SALE,SellState.RESERVATION);
        //entity리스트->dto리스트
        List<PostDto> postDtoOnSaleList = PostEntityDtoMapper.toDtoList(onSalePostList);

        Map map = new HashMap();
        map.put("onSaleAndRsvList", postDtoOnSaleList);
        map.put("soldList", postRepository.getSoldList(memId));

        return map;
    }

    //숨김 게시글들 반환
    @Override
    public List<PostDto> selectHidePost(String memId){
        Member member = memberRepository.findById(memId).orElseThrow();

        List<Post> postList = postRepository.findByMemberAndHideStateOrderByPostIdDesc(member,HideState.HIDE);
        //entity리스트->dto리스트
        List<PostDto> postDtoList = PostEntityDtoMapper.toDtoList(postList);

        return postDtoList;
    }

    @Override
    public List<ChatRoomDto> selectBuyersByPost(MemberDto memberDto, Long postId){
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        List<Long> ids = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .where(QChat.chat.from.eq(memberEntity).or(QChat.chat.to.eq(memberEntity)))
                .groupBy(QChat.chat.room.roomId)
                .fetch();

        List<ChatRoomDto> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.post.postId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(ReadState.NOTREAD))
                                                        .and(QChat.chat.from.ne(memberEntity))
                                                ),
                                        "unacknowledgedMessageCount"
                                )
                        )
                )
                .from(QChatRoom.chatRoom)
                .leftJoin(QChat.chat)
                .on(QChatRoom.chatRoom.roomId.eq(QChat.chat.room.roomId))
                .where(
                        (QChatRoom.chatRoom.seller.eq(memberEntity)
                                .or(QChatRoom.chatRoom.buyer.eq(memberEntity))
                        )
                                .and(QChatRoom.chatRoom.post.postId.eq(postId))
                                .and(QChat.chat.chatId.in(ids))
                )
                .orderBy(QChat.chat.createdTime.desc())
                .fetch();

        return chatRoomList;
    }

    @Override
    @Transactional
    public void insertWish(Long postId, String memId){
        Post post = postRepository.findById(postId).orElseThrow();
        Member member = memberRepository.findById(memId).orElseThrow();
        Wish wishEntity = Wish.builder()
                .post(post)
                .member(member)
                .build();

        wishRepository.save(wishEntity);
    }

    @Override
    @Transactional
    public void deleteWish(Long postId, String memId){
        Post post = postRepository.findById(postId).orElseThrow();
        Member member = memberRepository.findById(memId).orElseThrow();

        wishRepository.deleteByPostAndMember(post, member);

    }

    @Override
    public String isWishExist(Long postId, String memId){
        Post post = postRepository.findById(postId).orElseThrow();
        Member member = memberRepository.findById(memId).orElseThrow();

        Wish wish = wishRepository.findByPostAndMember(post, member);

        if(wish != null){
            return "exist";
        } else{
            return "none";
        }
    }

    @Override
    public Integer countWish(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        Integer wishCount = wishRepository.countByPost(post);
        return wishCount;
    }

    @Override
    public List<PostDto> selectPostListFromWish(String memId){

        QWish qWish = QWish.wish;
        List<Post> postList = jpaQueryFactory.select(qWish.post)
                                .from(qWish)
                                .where(qWish.member.memId.eq(memId))
                                .fetch();
        List<PostDto> postDtoList = PostEntityDtoMapper.toDtoList(postList);

        return postDtoList;
    }

    @Override
    public List<PostDto> searchPost(String loginMemId, String searchWord) {
        List<Post> postList = postRepository.searchPost(loginMemId, searchWord);
        return PostEntityDtoMapper.toDtoList(postList);
    }
    @Override
    public List<PostDto> selectPostListByCategory(MemberDto memberDto, Category category) {
        String loginMemId = null;
        Loc loginMemLoc = null;
        if(memberDto != null) {
            loginMemId = memberDto.getMemId();
            loginMemLoc = memberDto.getLoc();
        }
        List<Post> postList = postRepository.selectBoardPostByCategory(loginMemId, loginMemLoc, category);
        return PostEntityDtoMapper.toDtoList(postList);
    }

    @Override
    @Transactional
    public void updateHits(Long postId){
        QPost qPost = QPost.post;
        jpaQueryFactory.update(qPost)
                .set(qPost.hits, qPost.hits.add(1))
                .where(qPost.postId.eq(postId))
                .execute();
    }

    public List<PostDto> postListBrief(int limit, String memId) {
        List<Post> posts = postRepository.postListByLimit(limit, memId);
        return PostEntityDtoMapper.toDtoList(posts);
    }
}
