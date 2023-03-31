package com.alisoft.hatim.dto.request;

import com.alisoft.hatim.domain.reference.Gender;
import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotNull
    private Gender gender;

    @NotNull
    private Long languageId;
}
