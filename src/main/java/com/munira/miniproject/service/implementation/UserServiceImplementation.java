package com.munira.miniproject.service.implementation;

import com.munira.miniproject.constants.AppConstants;
import com.munira.miniproject.entity.UserEntity;
import com.munira.miniproject.model.UserDto;
import com.munira.miniproject.repository.UserRepository;
import com.munira.miniproject.service.UserService;
import com.munira.miniproject.utils.JWTUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImplementation implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new Exception("User already exists!!");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setAddress(user.getAddress());
        userEntity.setRole(user.getRole());

//        String publicUserId = JWTUtils.generateUserID(10);
//        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails =userRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(storedUserDetails,UserDto.class);
        String accessToken = JWTUtils.generateToken(userEntity.getEmail());
        returnedValue.setAccessToken(AppConstants.TOKEN_PREFIX + accessToken);

        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if(userEntity == null)
            throw new UsernameNotFoundException("No user found!");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }


    @Override
    public UserDto getUserByUserId(Long userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UsernameNotFoundException("No user found!");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if(userEntity==null)
            throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),userEntity.getPassword(),
                true,true,true,true,
                new ArrayList<>());
    }
}
