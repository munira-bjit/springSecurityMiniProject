package com.munira.miniproject.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Service
@Table(name = "reserve")
public class ReserveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reserveId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    private String reserveStatus;

    private LocalDate reserveDate;
}
