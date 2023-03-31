package com.alisoft.hatim.dto.error;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessDeniedErrorResponseDto {
    private String message;
}
