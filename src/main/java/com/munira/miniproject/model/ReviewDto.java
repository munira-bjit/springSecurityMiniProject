package com.munira.miniproject.model;


import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
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
public class ReviewDto {
    private Long reviewId;

    private Long userId;
    private Long bookId;

    private String reviewText;
    private String rating;
    private LocalDate date;
}
