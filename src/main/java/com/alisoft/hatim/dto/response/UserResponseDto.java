package com.alisoft.hatim.dto.response;

import com.alisoft.hatim.domain.reference.Gender;
import com.alisoft.hatim.domain.reference.Language;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    Long id;
    String userName;
    Gender gender;
    Language language;
}
