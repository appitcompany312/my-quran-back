package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.*;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.dto.UserDto;
import com.alisoft.hatim.exception.ConflictException;
import com.alisoft.hatim.exception.DuplicateException;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.NotValidArgumentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User get(Long id) throws NotFoundException;

    User create(SignUpForm form) throws NotFoundException;

    AuthResponse login(AuthForm form);
    AuthResponse signUp(SignUpForm form) throws NotFoundException;

    AuthResponse refresh(RefreshForm form);

    List<User> findAll();

    User findByUserName(String userName);

    User findById(Long id);

    User save(User user);

    User profile(JwtUser jwtUser);

    String getCurrentUsersUsername();

    void sendEmail(UserDto userDto) throws NotFoundException, ConflictException;

    AuthResponse confirmEmailVerificationToken(UserDto token, String verificationCode) throws DuplicateException, NotFoundException, NotValidArgumentException;
}
