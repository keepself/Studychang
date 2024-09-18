package com.changseon.StudyWithChang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailRes {

    private Long postId;
    private String title;
    private String contents;
    private String category;
    private String filePath;
    private LocalDateTime createdAt;

}
