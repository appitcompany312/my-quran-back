package com.alisoft.hatim.dto.response;

import com.alisoft.hatim.domain.reference.Gender;
import com.alisoft.hatim.domain.reference.Language;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String userName;
    private Gender gender;
    private Language language;
    private Boolean confirmed;
}
