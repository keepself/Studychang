package com.changseon.StudyWithChang.controller;

import com.changseon.StudyWithChang.domain.Post;
import com.changseon.StudyWithChang.dto.PostCreateReqDto;
import com.changseon.StudyWithChang.dto.PostDetailRes;
import com.changseon.StudyWithChang.dto.PostUpdateReq;
import com.changseon.StudyWithChang.global.common.CommonResponse;
import com.changseon.StudyWithChang.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;

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

        Post createdPost = postService.PostCreate(postCreateReqDto, file);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> PostList(Pageable pageable){
        return new ResponseEntity<>(new CommonResponse("포스트 리스트 출력", postService.findAll(pageable)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") long postId){
        postService.PostDelete(postId);
        return new ResponseEntity<>(new CommonResponse("게시물 삭제 성공", postId), HttpStatus.OK);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("postId") long postId,
                                                     @RequestPart("post") PostUpdateReq postUpdateReq,
                                                     @RequestPart(name = "file", required = false) MultipartFile multipartFile)throws IOException{

        postService.PostUpdate(postId, postUpdateReq, multipartFile);

        return new ResponseEntity<>(new CommonResponse("게시물 업데이트 완료",postId), HttpStatus.OK);

    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<CommonResponse> detailPost(@PathVariable("postId") long postId) {
       return new ResponseEntity<>(new CommonResponse("디테일페이지 성공", postService.PostDetail(postId)), HttpStatus.OK);
    }
}




