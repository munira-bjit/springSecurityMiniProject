package com.munira.miniproject.repository;

import com.munira.miniproject.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    boolean existsByBookId(Long bookId);
    BookEntity findByBookId(Long bookId);
    Optional<BookEntity> findByBookIdAndDeletedFalse(Long bookId);
    List<BookEntity> findAllByDeletedFalse();
}
