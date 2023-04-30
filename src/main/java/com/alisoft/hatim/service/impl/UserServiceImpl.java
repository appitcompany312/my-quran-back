package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.*;
import com.alisoft.hatim.domain.ConfirmationToken;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.dto.UserDto;
import com.alisoft.hatim.exception.ConflictException;
import com.alisoft.hatim.exception.DuplicateException;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.NotValidArgumentException;
import com.alisoft.hatim.repository.ConfirmationTokenRepository;
import com.alisoft.hatim.repository.RoleRepository;
import com.alisoft.hatim.repository.UserRepository;
import com.alisoft.hatim.service.EmailVerificationService;
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

import java.time.LocalDateTime;
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
    private final EmailVerificationService emailVerificationService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    @Transactional(readOnly = true)
    public User get(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id, "id"));
    }

    @Override
    public User create(SignUpForm form) throws NotFoundException {
        String uuid = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername(uuid);
        user.setPassword(hash("123456"));
        user.setGender(form.getGender());
        user.setLanguage(languageService.get(1L));
        user.setRole(roleRepository.findById(1L).get());
        user.setLanguageCode(form.getLanguageCode());
        user.setConfirmed(false);
        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(AuthForm form) throws BadCredentialsException {
        return authenticate(form.getUsername(), form.getPassword());
    }

    @Override
    public AuthResponse signUp(SignUpForm form) throws BadCredentialsException, NotFoundException {
        User user = create(form);
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

    @Override
    public void sendEmail(UserDto userDto) throws NotFoundException, ConflictException {
        User user = userRepository.findByUsername(userDto.getEmail());

        if (user != null && user.getConfirmed()) {
            log.error("Email {} already taken", userDto.getEmail());
            throw new RuntimeException("Email already taken");
        }

        user = userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException(
                String.format("User by id %s not found", userDto.getId())));

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenRepository.save(confirmationToken);
        log.info("Confirmation token saved: {}", confirmationToken.getToken());

        emailVerificationService.sendEmail(userDto.getEmail(), userDto.getName(), token);
        log.info("Verification token send to: {}", userDto.getEmail());
    }

    @Transactional
    public AuthResponse confirmEmailVerificationToken(UserDto userDto, String verificationCode) throws DuplicateException,
            NotFoundException, NotValidArgumentException {

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(verificationCode).orElseThrow(
                () -> new NotFoundException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            log.error("Email {} already confirmed", userDto.getEmail());
            throw new DuplicateException("Email already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            log.error("Confirmation token expired: {}", verificationCode);
            throw new NotValidArgumentException("Confirmation token expired");
        }

        confirmationTokenRepository.updateConfirmedAt(verificationCode, LocalDateTime.now());

        String encodedPassword = hash(userDto.getPassword());

        User user = confirmationToken.getUser();
        user.setConfirmed(true);
        user.setUsername(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        userRepository.save(user);
        log.info("User by username: {} updated", user.getUsername());

        return authenticate(user.getUsername(), userDto.getPassword());
    }
}
