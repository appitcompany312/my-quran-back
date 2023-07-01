package com.alisoft.hatim.config.security.jwt;

import com.alisoft.hatim.domain.reference.Gender;
import lombok.Data;

@Data
public class AuthForm {

    private String username;
    private Gender gender;
    private String languageCode;

}
