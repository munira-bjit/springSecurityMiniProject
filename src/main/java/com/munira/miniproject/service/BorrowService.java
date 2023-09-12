package com.munira.miniproject.service;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.model.BorrowDto;
import com.munira.miniproject.model.BorrowInfoDto;

import java.util.List;

public interface BorrowService {
    public BorrowDto bookBorrowing(Long bookId) throws Exception;
    public BorrowDto bookReturning(Long bookId) throws Exception;

    public List<BookEntity> getAllBookByUser(Long userId) throws Exception;

    public List<BorrowInfoDto> getUserAllHistory(Long userId) throws Exception;

}
