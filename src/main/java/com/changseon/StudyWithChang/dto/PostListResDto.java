package com.changseon.StudyWithChang.dto;

import com.changseon.StudyWithChang.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class PostListResDto {

    private Long postId;

    private String title;

    private String contents;

    private LocalDateTime createdAt;

    public static PostListResDto mapToPost(Post post){
        return PostListResDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
