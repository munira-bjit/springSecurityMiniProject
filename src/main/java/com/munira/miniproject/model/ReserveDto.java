package com.munira.miniproject.model;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveDto {

    private Long reserveId;
    private Long userId;
    private Long bookId;
    private String reserveStatus;
    private LocalDate reserveDate;

}
