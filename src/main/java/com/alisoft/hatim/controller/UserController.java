package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.request.UserRequestDto;
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
    @PostMapping("/create")
    public void create(
            @RequestBody UserRequestDto requestDto
    ) {
//        return userService.create(requestDto);
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
