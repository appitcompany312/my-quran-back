package com.alisoft.hatim.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnauthorizedErrorResponseDto {
    private String message;
}
