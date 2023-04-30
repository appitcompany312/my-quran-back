package com.alisoft.hatim.service;

import com.alisoft.hatim.exception.ConflictException;

public interface EmailVerificationService {
    void sendEmail(String email, String name, String token) throws ConflictException;
}
