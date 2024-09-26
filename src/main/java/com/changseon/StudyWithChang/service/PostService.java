package com.changseon.StudyWithChang.service;

import ch.qos.logback.classic.Logger;
import com.changseon.StudyWithChang.domain.Post;
import com.changseon.StudyWithChang.dto.PostDetailRes;
import com.changseon.StudyWithChang.dto.PostListResDto;
import com.changseon.StudyWithChang.dto.PostCreateReqDto;
import com.changseon.StudyWithChang.dto.PostUpdateReq;
import com.changseon.StudyWithChang.repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Builder
public class PostService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final String uploadDir = "/Users/changman/Desktop/photo1/"; // 업로드 디렉토리 경로

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post PostCreate(PostCreateReqDto postCreateReqDto) throws IOException {
        String fileName = null;
        String uploadUrl = null;

        for (int i = 0; i < postCreateReqDto.getFiles().size(); i++) {

            // 콘텐츠안에 있는 파일을 반복문을 통해 갯수 확인
            // 각 파일을 url 형태로 변환
            postCreateReqDto.setContents(
                    postCreateReqDto.getContents().replace(postCreateReqDto.getUrl().get(i), "http://localhost:8080/image/" + postCreateReqDto.getUrl().get(i))
            );
            // 파일이 있다면
            if (postCreateReqDto.getFiles().get(i) != null && !postCreateReqDto.getFiles().get(i).isEmpty()) {
                fileName = postCreateReqDto.getFiles().get(i).getOriginalFilename(); // 원본 파일 이름 가져오기
                String filePath = Paths.get(uploadDir, fileName).toString();// Paths 클래스를 사용해서 디렉토리와 파일원본이름을 결합 후 문자열로 반환
                File dest = new File(filePath);
                // 파일 저장
                try {
                    Files.createDirectories(Paths.get(uploadDir)); // 디렉토리 생성
                    postCreateReqDto.getFiles().get(i).transferTo(dest); // 파일 저장
                    uploadUrl = filePath; // 로컬 경로 설정
                } catch (IOException e) {
                    // 예외 처리
                    throw new IOException("파일 저장 중 오류가 발생했습니다.", e);
                }
            }
        }
        Post new_post = Post.builder()
                .title(postCreateReqDto.getTitle())
                .contents(postCreateReqDto.getContents())
                .createdAt(postCreateReqDto.getCreatedAt())
                .category(postCreateReqDto.getCategory())
                .build();
        // Post 객체에 파일 이름과 경로를 설정
        new_post.setFileName(fileName);
        new_post.setImagePath(uploadUrl);
        // 데이터베이스에 저장
        return postRepository.save(new_post);
    }

    public Page<PostListResDto> findAll(String category, Boolean delYN, Pageable pageable) {
        return postRepository.findAllByCategoryAndDelYNOrderByCreatedAtDesc(category, delYN, pageable)
                .map(PostListResDto::mapToPost);

    }

    public void PostDelete(long postId) {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        post.deletePost();
    }

    public void PostUpdate(long postId, PostUpdateReq postUpdateReq) throws IOException {
        // 기존 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        // 기존 파일 삭제
        String deleteFilename = post.getFileName();
        if (deleteFilename != null && !deleteFilename.isEmpty()) {
            File existingFile = new File(uploadDir + deleteFilename);
            if (existingFile.exists()) {
                if (!existingFile.delete()) {
                    throw new IOException("기존 파일 삭제 중 오류가 발생했습니다.");
                }
            }
        }



        // 파일 처리
        String fileName = null;
        String uploadUrl = null;

        for (int i = 0; i < postUpdateReq.getFiles().size(); i++) {
            // 파일이 있다면
            if (postUpdateReq.getFiles().get(i) != null && !postUpdateReq.getFiles().get(i).isEmpty()) {
                fileName = postUpdateReq.getFiles().get(i).getOriginalFilename(); // 원본 파일 이름 가져오기
                String filePath = Paths.get(uploadDir, fileName).toString(); // 파일 경로 설정
                File dest = new File(filePath);

                // 파일 저장
                try {
                    Files.createDirectories(Paths.get(uploadDir)); // 디렉토리 생성
                    postUpdateReq.getFiles().get(i).transferTo(dest); // 파일 저장
                    uploadUrl = filePath; // 로컬 경로 설정
                } catch (IOException e) {
                    throw new IOException("파일 저장 중 오류가 발생했습니다.", e);
                }
            }

            // 콘텐츠에 있는 URL 업데이트 (필요 시 적용)
            postUpdateReq.setContents(
                    postUpdateReq.getContents().replace(postUpdateReq.getUrl().get(i), "http://localhost:8080/image/" + postUpdateReq.getUrl().get(i))
            );
            log.info("zz" + postUpdateReq.getFiles().get(i).getOriginalFilename());
            log.info("se"+ postUpdateReq.getContents());
        }
        // 게시물 업데이트
        post.updatePost(postUpdateReq);
        // 파일 정보 설정
        post.setFileName(fileName);
        post.setImagePath(uploadUrl);

    }


    public PostDetailRes PostDetail(long postId) {

        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new EntityNotFoundException("해당하는 페이지가 존재하지 않습니다."));

        return PostDetailRes.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .contents(post.getContents())
                .category(post.getCategory())
                .filePath(post.getFilePath())
                .build();
    }

    public Resource getImage(String imagename) {
        Resource resource; // Resource 객체를 선언
        log.info(imagename);

        // 업로드 디렉토리와 이미지 이름을 결합하여 파일 경로를 생성
        Path path = Paths.get(uploadDir + imagename);

        try {
            // 생성된 경로를 URI로 변환하여 UrlResource 객체를 생성
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url error");
        }

        // 생성된 Resource 객체를 반환
        return resource;
    }
};
