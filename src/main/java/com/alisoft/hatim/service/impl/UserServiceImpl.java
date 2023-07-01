package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.*;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.repository.RoleRepository;
import com.alisoft.hatim.repository.UserRepository;
import com.alisoft.hatim.service.UserService;
import com.alisoft.hatim.service.reference.LanguageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;


@Data
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final LanguageService languageService;

    @Override
    @Transactional(readOnly = true)
    public User get(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id, "id"));
    }

    @Override
    public User create(AuthForm form) throws NotFoundException {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(hash("123456"));
        user.setGender(form.getGender());
        user.setLanguage(languageService.get(1L));
        user.setRole(roleRepository.findById(1L).get());
        user.setLanguageCode(form.getLanguageCode());
        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(AuthForm form) throws BadCredentialsException, NotFoundException {
        User user;
        String password = "123456";
        if (isNull(form.getUsername()) || form.getUsername().isBlank()) {
            String randomUsername = UUID.randomUUID().toString();
            form.setUsername(randomUsername);
            user = create(form);
            return authenticate(user.getUsername(), password);
        }

        user = findByUserName(form.getUsername());

        if (isNull(user)) {
            user = create(form);
            return authenticate(user.getUsername(), password);
        }

        return authenticate(form.getUsername(), password);
    }

    @Override
    public AuthResponse signUp(SignUpForm form) throws BadCredentialsException, NotFoundException {
        AuthForm authForm = new AuthForm();
        authForm.setGender(form.getGender());
        authForm.setLanguageCode(form.getLanguageCode());
        authForm.setUsername(UUID.randomUUID().toString());
        User user = create(authForm);
        return authenticate(user.getUsername(), "123456");
    }

    @Override
    public AuthResponse refresh(RefreshForm form) {
        try {
            String newAccessToken = jwtTokenProvider.createAccessTokenRefresh(form.getRefreshToken());
            String newRefreshToken = jwtTokenProvider.createRefreshTokenRefresh(form.getRefreshToken());
            return new AuthResponse(jwtTokenProvider.getUsername(newRefreshToken), newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }

    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User profile(JwtUser jwtUser) {
        return findById(jwtUser.getId());
    }

    @Override
    public String getCurrentUsersUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userRepository.findAll();
        log.info("All users {}", allUsers);
        return allUsers;
    }

    @Override
    public User findByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
        log.info("Find by user_name {}", user);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (isNull(user)) {
            log.warn("Not found by ID user {}, ", id);
            return null;
        }
        log.info("Find by ID user {}, ", user);

        return user;
    }

    private AuthResponse authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = findByUserName(username);
            if (isNull(user)) {
                throw new UsernameNotFoundException("User does not exists");
            }
            String token = jwtTokenProvider.createAccessTokenLogin(username);
            String refreshToken = jwtTokenProvider.createRefreshTokenLogin(username);

            return new AuthResponse(user.getUsername(), token, refreshToken);

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
