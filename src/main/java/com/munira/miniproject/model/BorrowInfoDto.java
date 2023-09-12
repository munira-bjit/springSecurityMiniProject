package com.munira.miniproject.model;

import com.munira.miniproject.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowInfoDto {
    private Long borrowId;
    private BookEntity bookEntity;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
