package com.munira.miniproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.munira.miniproject.SpringApplicationContext;
import com.munira.miniproject.constants.AppConstants;
import com.munira.miniproject.model.UserDto;
import com.munira.miniproject.model.UserLoginRequestModel;
import com.munira.miniproject.service.UserService;
import com.munira.miniproject.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword())
            );
        } catch (IOException e) {
            log.info("Exception occurred at attemptAuthentication method: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String user = ((User) authResult.getPrincipal()).getUsername();
        String accessToken = JWTUtils.generateToken(user);
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplementaion");
        UserDto userDto = userService.getUser(user);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login Successful");
        responseBody.put("userId", userDto.getUserId());
        responseBody.put("email", userDto.getEmail());
        responseBody.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyJSON = objectMapper.writeValueAsString(responseBody);
        response.setContentType("application/json");
        response.getWriter().write(responseBodyJSON);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unsuccessful Authentication");
        errorResponse.put("message", "Provide valid email or password");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseJson);
    }
}
