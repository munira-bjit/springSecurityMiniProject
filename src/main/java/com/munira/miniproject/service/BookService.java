package com.munira.miniproject.service;

import com.munira.miniproject.model.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook (BookDto bookDto) throws Exception;
    BookDto updateBook (BookDto bookDto) throws Exception;
    void deleteBook (BookDto bookDto) throws Exception;
    List<BookDto> getAllBook() throws Exception;
//    public BookDto getBookByBookId(Long bookId) throws Exception;
}
