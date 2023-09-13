package com.munira.miniproject.repository;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.BorrowEntity;
import com.munira.miniproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {
    BorrowEntity findByUserEntityAndBookEntityAndReturnDateIsNull(UserEntity userEntity, BookEntity bookEntity);
    List<BorrowEntity> findAllByUserEntity(UserEntity userEntity);
    List<BorrowEntity> findAllByUserEntityAndReturnDateIsNull(UserEntity userEntity);
}
