package com.alisoft.hatim.config.security.jwt;

import com.alisoft.hatim.domain.reference.Gender;
import lombok.Data;

@Data
public class SignUpForm {
    private Gender gender;

    private String languageCode;
}
