package com.munira.miniproject.controller;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.exception.BookNotFoundException;
import com.munira.miniproject.exception.BookWasNotBorrowedException;
import com.munira.miniproject.exception.ReviewNotFoundException;
import com.munira.miniproject.exception.UnauthorizedException;
import com.munira.miniproject.model.*;
import com.munira.miniproject.service.implementation.BookServiceImplementation;
import com.munira.miniproject.service.implementation.BorrowServiceImplementation;
import com.munira.miniproject.service.implementation.ReserveServiceImplementation;
import com.munira.miniproject.service.implementation.ReviewServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookServiceImplementation bookServiceImplementation;

    @Autowired
    private BorrowServiceImplementation borrowServiceImplementation;

    @Autowired
    private ReviewServiceImplementation reviewServiceImplementation;

    @Autowired
    private ReserveServiceImplementation reserveServiceImplementation;

    @PostMapping("/books/create")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto newBook = bookServiceImplementation.createBook(bookDto);
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/books/update")
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookServiceImplementation.updateBook(bookDto);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/books/delete")
    public ResponseEntity<?> deleteBook(@RequestBody BookDto bookDto) {
        try {
            bookServiceImplementation.deleteBook(bookDto);
            return new ResponseEntity<>("Book Deleted Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/all")
    public ResponseEntity<?> allBooks() {
        try {
            List<BookDto> allBook = bookServiceImplementation.getAllBook();
            return new ResponseEntity<>(allBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/books/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId) {
        try {
            BorrowDto book = borrowServiceImplementation.bookBorrowing(bookId);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/{bookId}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        try {
            BorrowDto book = borrowServiceImplementation.bookReturning(bookId);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookWasNotBorrowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/{userId}/books")
    public ResponseEntity<?> retriveBooks(@PathVariable Long userId) {
        try {
            List<BookEntity> allBookByUser = borrowServiceImplementation.getAllBookByUser(userId);
            return new ResponseEntity<>(allBookByUser, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BookWasNotBorrowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{userId}/borrowed-books")
    public ResponseEntity<?> retriveBorrowedBooks(@PathVariable Long userId) {
        try {
            List<BookEntity> allBookByUser = borrowServiceImplementation.getAllBorrowedBookByUser(userId);
            return new ResponseEntity<>(allBookByUser, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BookWasNotBorrowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/{userId}/history")
    public ResponseEntity<?> userHistory(@PathVariable Long userId) {
        try {
            List<BorrowInfoDto> userAllHistory = borrowServiceImplementation.getUserAllHistory(userId);
            return new ResponseEntity<>(userAllHistory, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/books/{bookId}/reviews/create")
    public ResponseEntity<?> createReview(@PathVariable Long bookId, @RequestBody ReviewDto bookReviewDto) {
        try {
            ReviewDto newReview = reviewServiceImplementation.createBookReview(bookId, bookReviewDto);
            return new ResponseEntity<>(newReview, HttpStatus.CREATED);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> allReview(@PathVariable Long bookId) {
        try {
            List<ReviewDto> newReview = reviewServiceImplementation.allBookReview(bookId);
            return new ResponseEntity<>(newReview, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/books/{bookId}/reviews/{reviewId}/delete")
    public ResponseEntity<?> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
        try {
            reviewServiceImplementation.deleteReview(bookId, reviewId);
            return new ResponseEntity<>("Review Deleted!", HttpStatus.CREATED);
        } catch (BookNotFoundException | ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/books/{bookId}/reviews/{reviewId}/update")
    public ResponseEntity<?> updateReview(@PathVariable Long bookId, @PathVariable Long reviewId,
                                          @RequestBody ReviewDto bookReviewDto) {
        try {
            ReviewDto updatedReview = reviewServiceImplementation.updateReview(bookId, reviewId, bookReviewDto);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (BookNotFoundException | ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/books/{bookId}/reserve")
    public ResponseEntity<?> reserveBook(@PathVariable Long bookId) {
        try {
            ReserveDto updatedReview = reserveServiceImplementation.reserveBook(bookId);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/books/{bookId}/cancel-reservation")
    public ResponseEntity<?> cancelReserveBook(@PathVariable Long bookId) {
        try {
            ReserveDto cancelReview = reserveServiceImplementation.cancelReserveBook(bookId);
            return new ResponseEntity<>(cancelReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
