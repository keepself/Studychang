package com.changseon.StudyWithChang.controller;

import com.changseon.StudyWithChang.domain.Post;
import com.changseon.StudyWithChang.dto.PostCreateReqDto;
import com.changseon.StudyWithChang.dto.PostDetailRes;
import com.changseon.StudyWithChang.dto.PostListResDto;
import com.changseon.StudyWithChang.dto.PostUpdateReq;
import com.changseon.StudyWithChang.global.common.CommonResponse;
import com.changseon.StudyWithChang.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
            @RequestPart("post") PostCreateReqDto postCreateReqDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        // createdAt 값이 null일 경우 현재 시간으로 설정
        if (postCreateReqDto.getCreatedAt() == null) {
            postCreateReqDto.setCreatedAt(LocalDateTime.now());
        }

        log.info("Creating post with details: {}", postCreateReqDto);

        // 파일이 존재할 경우 처리
        if (file != null && !file.isEmpty()) {
            log.info("File uploaded: {}", file.getOriginalFilename());
        } else {
            log.info("No file uploaded.");
        }

        // 포스트 생성 로직을 호출, 파일이 null일 때도 처리 가능하도록 변경
        Post createdPost = postService.PostCreate(postCreateReqDto, file);

        // 로그에 createdAt 값 출력
        log.info("Post created with details: title={}, category={}, contents={}, createdAt={}",
                createdPost.getTitle(), createdPost.getCategory(), createdPost.getContents(), createdPost.getCreatedAt());

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }



    @GetMapping("/list")
    public ResponseEntity<CommonResponse> postList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "delYN", required = false, defaultValue = "false") Boolean delYN,
            Pageable pageable) {


        // 서비스 메서드 호출하여 데이터 가져오기
        Page<PostListResDto> posts = postService.findAll(category, delYN, pageable);


        // 응답 반환
        return new ResponseEntity<>(new CommonResponse("포스트 리스트 출력", posts), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") long postId){
        log.info("Deleting post with ID: {}", postId);
        postService.PostDelete(postId);
        return new ResponseEntity<>(new CommonResponse("게시물 삭제 성공", postId), HttpStatus.OK);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("postId") long postId,
                                                     @RequestPart("post") PostUpdateReq postUpdateReq,
                                                     @RequestPart(name = "file", required = false) MultipartFile multipartFile) throws IOException {

        log.info("Updating post with ID: {}", postId);
        log.info("Post update details: {}", postUpdateReq);
        if (multipartFile != null) {
            log.info("File uploaded: {}", multipartFile.getOriginalFilename());
        }

        postService.PostUpdate(postId, postUpdateReq, multipartFile);

        return new ResponseEntity<>(new CommonResponse("게시물 업데이트 완료", postId), HttpStatus.OK);
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<CommonResponse> detailPost(@PathVariable("postId") long postId) {
        log.info("Fetching details for post ID: {}", postId);
        return new ResponseEntity<>(new CommonResponse("디테일페이지 성공", postService.PostDetail(postId)), HttpStatus.OK);
    }
}
