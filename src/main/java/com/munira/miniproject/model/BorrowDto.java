package com.munira.miniproject.model;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowDto {
    private Long borrowId;

    private Long userId;
    private Long bookId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
