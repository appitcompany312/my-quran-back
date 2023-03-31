package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.AuthForm;
import com.alisoft.hatim.config.security.jwt.AuthResponse;
import com.alisoft.hatim.config.security.jwt.RefreshForm;
import com.alisoft.hatim.config.security.jwt.SignUpForm;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/sign_in")
    public AuthResponse signIn(@RequestBody AuthForm form) {
        return userService.login(form);
    }

    @PostMapping("/sign_up")
    public AuthResponse signUp(@RequestBody SignUpForm form) throws NotFoundException {
        return userService.signUp(form);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshForm form) {
        return userService.refresh(form);
    }

}
