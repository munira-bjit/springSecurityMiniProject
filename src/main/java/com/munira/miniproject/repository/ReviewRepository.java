package com.munira.miniproject.repository;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByBookEntity(BookEntity bookEntity);
    ReviewEntity findByReviewId(Long reviewId);
}
