package com.changseon.StudyWithChang.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "LONGTEXT")    private String contents;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    private Boolean delYN = false;

    @Column(nullable = false)
    private String category;

    @Column
    private String imageUrl;  // 이미지 URL 필드 추가



    public void setImagePath(String filePath) {
        this.filePath = filePath;
    }

    public void deletePost() {
        this.delYN = true;
    }
    public void updatePost(String title, String contents, String fileName)  {
        this.title = title;
        this.contents = contents;
        this.fileName = fileName;
    }

}
