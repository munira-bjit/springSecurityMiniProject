package com.munira.miniproject.service;

import com.munira.miniproject.model.UserDto;

public interface UserService {
    UserDto createUser(UserDto user) throws Exception;
    UserDto getUser(String email);

    UserDto getUserByUserId(Long id) throws Exception;


}
