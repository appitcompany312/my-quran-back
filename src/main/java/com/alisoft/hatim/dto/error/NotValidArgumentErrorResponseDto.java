package com.alisoft.hatim.dto.error;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class NotValidArgumentErrorResponseDto {
    private Map<String, String> values;

    public NotValidArgumentErrorResponseDto(String field, String message) {
        values = Collections.singletonMap(field, message);
    }

    public NotValidArgumentErrorResponseDto(Map<String, String> errors){
        values = errors;
    }

}
