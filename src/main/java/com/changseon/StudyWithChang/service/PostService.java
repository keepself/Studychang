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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Transactional
@Builder
public class PostService {

    private final PostRepository postRepository;
    private final String uploadDir = "/Users/changman/Desktop/photo1/"; // 업로드 디렉토리 경로

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post PostCreate(PostCreateReqDto postCreateReqDto, MultipartFile multipartFile) throws IOException {
        String fileName = null;
        String uploadUrl = null;

        Post new_post = Post.builder()
                .title(postCreateReqDto.getTitle())
                .contents(postCreateReqDto.getContents())
                .category(postCreateReqDto.getCategory())
                .build();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            fileName = multipartFile.getOriginalFilename(); // 원본 파일 이름 가져오기
            String filePath = Paths.get(uploadDir, fileName).toString();
            File dest = new File(filePath);
            // 파일 저장
            try {
                Files.createDirectories(Paths.get(uploadDir)); // 디렉토리 생성
                multipartFile.transferTo(dest); // 파일 저장
                uploadUrl = filePath; // 로컬 경로 설정
            } catch (IOException e) {
                // 예외 처리
                throw new IOException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }

        // Post 객체에 파일 이름과 경로를 설정
        new_post.setFileName(fileName);
        new_post.setImagePath(uploadUrl);
        // 데이터베이스에 저장
        return postRepository.save(new_post);
    }
    public Page<PostListResDto> findAll(String category,Boolean delYN,Pageable pageable) {
        return postRepository.findAllByCategoryAndDelYNOrderByCreatedAtDesc(category,delYN,pageable)
                .map(PostListResDto::mapToPost);

    }
    public void PostDelete(long postId){
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        post.deletePost();
    }
    public void PostUpdate(long postId, PostUpdateReq postUpdateReq, MultipartFile multipartFile) throws IOException {
        // 기존 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        String deletefilename = post.getFileName();

        // 게시물 업데이트
        post.updatePost(postUpdateReq.getTitle(), postUpdateReq.getContents(),multipartFile.getOriginalFilename());
        // 파일 처리
        if (multipartFile != null && !multipartFile.isEmpty()) {
            // 기존 파일 삭제
            if (post.getFileName() != null && !post.getFileName().isEmpty()) {
                File existingFile = new File(uploadDir + deletefilename);
                if (existingFile.exists()) {
                    if (!existingFile.delete()) {
                        throw new IOException("기존 파일 삭제 중 오류가 발생했습니다.");
                    }
                }
            }

            // 새 파일 저장
            String newFileName = multipartFile.getOriginalFilename();
            String filePath = Paths.get(uploadDir, newFileName).toString();
            File destinationFile = new File(filePath);

            // 디렉토리 생성
            if (!destinationFile.getParentFile().exists()) {
                if (!destinationFile.getParentFile().mkdirs()) {
                    throw new IOException("디렉토리 생성 중 오류가 발생했습니다.");
                }
            }

            try {
                multipartFile.transferTo(destinationFile);
                post.setImagePath(filePath); // 새로운 파일 경로 설정
            } catch (IOException e) {
                // 예외 처리 및 로깅
                Logger logger = (Logger) LoggerFactory.getLogger(PostService.class);
                logger.error("파일 저장 중 오류가 발생했습니다.", e);
                throw new IOException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }
    }
    public PostDetailRes PostDetail(long postId){

    Post post = postRepository.findByPostId(postId).orElseThrow(()-> new EntityNotFoundException("해당하는 페이지가 존재하지 않습니다."));

        return PostDetailRes.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .category(post.getCategory())
                .filePath(post.getFilePath())
                .build();
    }
}