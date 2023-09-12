package com.munira.miniproject.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "borrow")
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long borrowId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
