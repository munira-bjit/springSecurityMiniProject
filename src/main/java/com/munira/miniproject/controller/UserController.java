package com.munira.miniproject.controller;

import com.munira.miniproject.constants.AppConstants;
import com.munira.miniproject.model.UserDto;
import com.munira.miniproject.model.UserLoginRequestModel;
import com.munira.miniproject.service.UserService;
import com.munira.miniproject.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/users/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            String accessToken = JWTUtils.generateToken(createdUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sign-up Successful!!");
            response.put("user", createdUser);
            response.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestModel userLoginReqModel,
                                   HttpServletResponse response) throws IOException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginReqModel.getEmail(), userLoginReqModel.getPassword()));
            if (authentication.isAuthenticated()) {
                UserDto userDto = userService.getUser(userLoginReqModel.getEmail());
                String accessToken = JWTUtils.generateToken(userDto.getEmail());

                Map<String, Object> loginResponse = new HashMap<>();
                loginResponse.put("message", "Log in Successful!!");
                loginResponse.put("userId", userDto.getUserId());
                loginResponse.put("email", userDto.getEmail());
                loginResponse.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

            }
            else {
                return new ResponseEntity<>("Invalid email or password!", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid email or password!", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> userDetailsByUserId(@PathVariable Long userId) {
        try {
            UserDto user = userService.getUserByUserId(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
