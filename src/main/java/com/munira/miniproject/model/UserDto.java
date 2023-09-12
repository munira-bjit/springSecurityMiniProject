package com.munira.miniproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String role;

    public void setAccessToken(String s) {
    }
}
