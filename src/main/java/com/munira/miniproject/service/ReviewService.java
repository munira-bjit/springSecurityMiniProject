package com.munira.miniproject.service;

import com.munira.miniproject.model.ReviewDto;

import java.util.List;

public interface ReviewService {
    public ReviewDto createBookReview(Long bookId, ReviewDto bookReviewDto) throws Exception;
    public List<ReviewDto> allBookReview(Long bookId) throws Exception;

    public ReviewDto updateReview(Long bookId, Long reviewId, ReviewDto bookReviewDto) throws Exception;

    public void deleteReview(Long bookId, Long reviewId) throws Exception;

}
