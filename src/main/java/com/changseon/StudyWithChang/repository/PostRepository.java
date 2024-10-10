package com.changseon.StudyWithChang.repository;

import com.changseon.StudyWithChang.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByCategoryAndDelYNOrderByCreatedAtDesc(String category, Boolean delYN, Pageable pageable);

    Optional<Post> findByPostId(long postId);

}



