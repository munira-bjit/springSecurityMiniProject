package com.munira.miniproject.service.implementation;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.ReviewEntity;
import com.munira.miniproject.entity.UserEntity;
import com.munira.miniproject.exception.BookNotFoundException;
import com.munira.miniproject.exception.ReviewNotFoundException;
import com.munira.miniproject.model.ReviewDto;
import com.munira.miniproject.repository.BookRepository;
import com.munira.miniproject.repository.ReviewRepository;
import com.munira.miniproject.repository.UserRepository;
import com.munira.miniproject.service.ReviewService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class ReviewServiceImplementation implements ReviewService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewDto createBookReview(Long bookId, ReviewDto bookReviewDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if (bookEntity == null || bookEntity.isDeleted())
            throw new BookNotFoundException("Book with ID " + bookId + " is not available");

        ModelMapper modelMapper = new ModelMapper();
        ReviewEntity bookReviewEntity = new ReviewEntity();

        bookReviewEntity.setBookEntity(bookEntity);
        bookReviewEntity.setUserEntity(userEntity);
        bookReviewEntity.setReviewText(bookReviewDto.getReviewText());
        bookReviewEntity.setRating(bookReviewDto.getRating());
        bookReviewEntity.setDate(LocalDate.now());


        ReviewEntity storeReview = reviewRepository.save(bookReviewEntity);

        return modelMapper.map(storeReview, ReviewDto.class);


    }

    public List<ReviewDto> allBookReview(Long bookId) throws Exception {
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if(bookEntity == null || bookEntity.isDeleted())
            throw new BookNotFoundException("Book with ID " + bookId + " is not available");

        ModelMapper modelMapper = new ModelMapper();
        List<ReviewEntity> bookReviews = reviewRepository.findAllByBookEntity(bookEntity);
        if (bookReviews.isEmpty())
            throw new ReviewNotFoundException("No review is found for this book :(");

        return bookReviews.stream()
                .map(reviewEntity -> modelMapper.map(reviewEntity, ReviewDto.class))
                .collect(Collectors.toList());
    }


    public void deleteReview(Long bookId, Long reviewId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();

        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null)
            throw new BookNotFoundException("This book is not found!!");

        ReviewEntity bookReview = reviewRepository.findByReviewId(reviewId);
        if (bookReview == null)
            throw new ReviewNotFoundException("Enter a valid review id!!");

        Long userId = bookReview.getUserEntity().getUserId();

        if (!currentUserId.equals(userId) && currentUserRole.equals("USER")) {
            throw new Exception("You are not authorized to access this!");
        }

        if (!bookReview.getBookEntity().getBookId().equals(bookId)) {
            throw new Exception("Invalid Book id or Review id!");
        }

        reviewRepository.delete(bookReview);


    }

    public ReviewDto updateReview(Long bookId, Long reviewId, ReviewDto bookReviewDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();

        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null)
            throw new BookNotFoundException("This book is not found!!");

        ReviewEntity bookReview = reviewRepository.findByReviewId(reviewId);
        if (bookReview == null)
            throw new ReviewNotFoundException("Enter a valid review id!!");

        Long userId = bookReview.getUserEntity().getUserId();

        if (!currentUserId.equals(userId) && currentUserRole.equals("USER")) {
            throw new Exception("You are not authorized to access this!");
        }

        if (!bookReview.getBookEntity().getBookId().equals(bookId)) {
            throw new Exception("Invalid Book id or Review id!");
        }

        bookReview.setRating(bookReviewDto.getRating());
        bookReview.setReviewText(bookReviewDto.getReviewText());
        bookReview.setDate(LocalDate.now());

        reviewRepository.save(bookReview);
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(bookReview, ReviewDto.class);
    }
}
