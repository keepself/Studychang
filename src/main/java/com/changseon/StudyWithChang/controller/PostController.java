package com.changseon.StudyWithChang.controller;

import com.changseon.StudyWithChang.domain.Post;
import com.changseon.StudyWithChang.dto.PostCreateReqDto;
import com.changseon.StudyWithChang.dto.PostDetailRes;
import com.changseon.StudyWithChang.dto.PostListResDto;
import com.changseon.StudyWithChang.dto.PostUpdateReq;
import com.changseon.StudyWithChang.global.common.CommonResponse;
import com.changseon.StudyWithChang.service.PostService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.List;

@Slf4j
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

        @PostMapping("/create")
        public ResponseEntity<Post> createPost( PostCreateReqDto postCreateReqDto ) throws IOException {

            if (postCreateReqDto.getCreatedAt() == null) {
                postCreateReqDto.setCreatedAt(LocalDateTime.now());
            }
            Post createdPost = postService.PostCreate(postCreateReqDto);
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
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("postId") long postId,PostUpdateReq postUpdateReq)
            throws IOException {
        log.info("kkkk" +postUpdateReq.getContents());

        postService.PostUpdate(postId, postUpdateReq);
        return new ResponseEntity<>(new CommonResponse("게시물 업데이트 완료", postId), HttpStatus.OK);
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<CommonResponse> detailPost(@PathVariable("postId") long postId) {
        log.info("Fetching details for post ID: {}", postId);

        // 게시물 상세 정보를 가져옵니다.
        PostDetailRes postDetail = postService.PostDetail(postId);

        // postId가 존재할 경우 "업데이트 불러오기 성공" 메시지를 반환합니다.
        if (postDetail != null) {
            return new ResponseEntity<>(new CommonResponse("업데이트 불러오기 성공", postDetail), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResponse("존재하지 않는 게시물입니다.", null), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/image/{imagename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getItem(@PathVariable("imagename") String imagename){
        Resource resource = postService.getImage(imagename);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }
}
