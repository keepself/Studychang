package com.changseon.StudyWithChang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateReqDto {
        private String title;
        private String contents;
        private String category;
        private LocalDateTime createdAt;
        private List<String> Url;  // 이미지 URL 필드 추가
        private List<MultipartFile> files = new ArrayList<>();
}
