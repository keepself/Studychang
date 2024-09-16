package com.changseon.StudyWithChang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateReqDto {

        private String title;
        private String contents;
        private String category;
        private String imageUrl;  // 이미지 URL 필드 추가



}
