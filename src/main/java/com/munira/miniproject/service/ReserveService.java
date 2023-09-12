package com.munira.miniproject.service;

import com.munira.miniproject.model.ReserveDto;

public interface ReserveService {
    public ReserveDto reserveBook(Long bookId) throws Exception;
    public ReserveDto cancelReserveBook(Long bookId) throws Exception;

}
