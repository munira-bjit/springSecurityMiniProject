package com.munira.miniproject.repository;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.ReserveEntity;
import com.munira.miniproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {

//    ReserveEntity findByBookEntity(BookEntity bookEntity);
//    List<ReserveEntity> findAllByBookEntityAndDeletedFalse(BookEntity bookEntity);

    List<ReserveEntity> findAllByBookEntityAndReserveStatus(BookEntity bookEntity, String pending);

    ReserveEntity findByUserEntityAndBookEntityAndReserveStatus(UserEntity userEntity, BookEntity bookEntity, String pending);
}
