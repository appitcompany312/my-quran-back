package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.AuthResponse;
import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.UserDto;
import com.alisoft.hatim.exception.ConflictException;
import com.alisoft.hatim.exception.DuplicateException;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.NotValidArgumentException;
import com.alisoft.hatim.mapper.UserMapper;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody UserDto userDto) throws NotFoundException, ConflictException {
        userService.sendEmail(userDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/confirm")
    public AuthResponse confirm(@RequestBody UserDto userDto, @RequestParam String verificationCode)
            throws DuplicateException, NotValidArgumentException, NotFoundException {
        return userService.confirmEmailVerificationToken(userDto, verificationCode);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(userMapper.userToUserResponseDto(userService.profile(jwtUser)));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.userToUserResponseDto(userService.findById(id)));
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userMapper.userListToUserResponseDtoList(userService.findAll()));
    }
}
